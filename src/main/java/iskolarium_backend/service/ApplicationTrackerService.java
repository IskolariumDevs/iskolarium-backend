package iskolarium_backend.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iskolarium_backend.dto.ApplicationRequestDto;
import iskolarium_backend.dto.ApplicationResponseDto;
import iskolarium_backend.dto.ChecklistItemDto;
import iskolarium_backend.entity.ApplicationTracker;
import iskolarium_backend.entity.ChecklistItem;
import iskolarium_backend.entity.Scholarship;
import iskolarium_backend.entity.UserAccount;
import iskolarium_backend.repository.ApplicationTrackerRepository;
import iskolarium_backend.repository.ChecklistItemRepository;
import iskolarium_backend.repository.ScholarshipRepository;
import iskolarium_backend.repository.UserAccountRepository;

@Service
public class ApplicationTrackerService {

    @Autowired
    private ApplicationTrackerRepository trackerRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private ScholarshipRepository scholarshipRepository;

    @Autowired 
    private ChecklistItemRepository checklistRepository;

    public ApplicationTracker submitApplication(ApplicationRequestDto dto, String userEmail) {
        UserAccount user = userAccountRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Scholarship scholarship = scholarshipRepository.findById(dto.getScholarshipId())
                .orElseThrow(() -> new RuntimeException("Scholarship not found"));

        boolean alreadyTracking = trackerRepository.existsByUserAccountAndScholarship(user, scholarship);

        if (alreadyTracking) {
            throw new RuntimeException("Error: You are already tracking this scholarship!");
        }

        ApplicationTracker tracker = new ApplicationTracker();
        tracker.setUserAccount(user);
        tracker.setScholarship(scholarship);
        tracker.setStatus("PENDING");
        tracker.setSubmissionDate(LocalDate.now()); 

        List<ChecklistItem> checklist = new ArrayList<>();
        
        if (scholarship.getRequirements() != null) {
            for (String reqName : scholarship.getRequirements()) {
                ChecklistItem item = new ChecklistItem();
                item.setRequirementName(reqName); 
                item.setIsCompleted(false);       
                item.setTracker(tracker);         
                checklist.add(item);
            }
        }
        
        tracker.setChecklistItems(checklist);
        return trackerRepository.save(tracker);
    }

    public List<ApplicationResponseDto> getStudentApplications(Long accountId) {
        List<ApplicationTracker> trackers = trackerRepository.findByUserAccount_AccountId(accountId);
        List<ApplicationResponseDto> responseList = new ArrayList<>();

        for (ApplicationTracker tracker : trackers) {
            ApplicationResponseDto dto = new ApplicationResponseDto();
            dto.setTrackerId(tracker.getTrackerId());
            dto.setScholarshipTitle(tracker.getScholarship().getTitle());
            dto.setProvider(tracker.getScholarship().getProvider());
            dto.setStatus(tracker.getStatus());
            dto.setSubmissionDate(tracker.getSubmissionDate());
            
            // Add new scholarship details
            List<ChecklistItemDto> checklistDtos = new ArrayList<>();

        if (tracker.getChecklistItems() != null) {
            for (ChecklistItem item : tracker.getChecklistItems()) {
            ChecklistItemDto itemDto = new ChecklistItemDto();
            itemDto.setId(item.getItemId());
            itemDto.setRequirementName(item.getRequirementName());
            itemDto.setCompleted(item.getIsCompleted());
            checklistDtos.add(itemDto);
            }
        }
            dto.setRequirements(checklistDtos);
            dto.setBenefits(tracker.getScholarship().getBenefits());
            dto.setApplicationLink(tracker.getScholarship().getApplicationLink());
            dto.setDeadlineDate(tracker.getScholarship().getDeadlineDate());
            
            // Calculate deadline warnings
            if (tracker.getScholarship().getDeadlineDate() != null) {
                long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), tracker.getScholarship().getDeadlineDate());
                dto.setDaysRemaining(daysRemaining);
                dto.setIsPriorityWarning(isPriorityDeadline(daysRemaining));
            }
            
            responseList.add(dto);
        }
        return responseList;
    }
    
    private boolean isPriorityDeadline(long daysRemaining) {
        return daysRemaining == 30 || daysRemaining == 7 || daysRemaining == 3;
    }

    public List<ApplicationResponseDto> getStudentApplicationsByEmail(String email) {
        UserAccount user = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return getStudentApplications(user.getAccountId());
    }

    public void updateChecklistItemStatus(Long checklistItemId, boolean isCompleted) {
        ChecklistItem item = checklistRepository.findById(checklistItemId)
                .orElseThrow(() -> new RuntimeException("Checklist item not found"));
        
        item.setIsCompleted(isCompleted);
        checklistRepository.save(item);
    }

    public void deleteApplication(Long id) {
        if (!trackerRepository.existsById(id)) {
            throw new RuntimeException("Application record not found.");
        }
        trackerRepository.deleteById(id);
    }
}