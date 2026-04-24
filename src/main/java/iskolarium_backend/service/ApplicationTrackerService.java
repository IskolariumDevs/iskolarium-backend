package iskolarium_backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iskolarium_backend.dto.ApplicationRequestDto;
import iskolarium_backend.dto.ApplicationResponseDto;
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



    public ApplicationTracker submitApplication(ApplicationRequestDto dto) {
        UserAccount user = userAccountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Scholarship scholarship = scholarshipRepository.findById(dto.getScholarshipId())
                .orElseThrow(() -> new RuntimeException("Scholarship not found"));

        ApplicationTracker tracker = new ApplicationTracker();
        tracker.setUserAccount(user);
        tracker.setScholarship(scholarship);
        tracker.setStatus("PENDING");
        tracker.setSubmissionDate(LocalDate.now()); 

        List<ChecklistItem> checklist = new ArrayList<>();
        
        // Loop through the DOST requirements (Form E, PSA, etc.)
        if (scholarship.getRequirements() != null) {
            for (String reqName : scholarship.getRequirements()) {
                ChecklistItem item = new ChecklistItem();
                item.setRequirementName(reqName); // Perfect match!
                item.setIsCompleted(false);       // Perfect match!
                item.setTracker(tracker);         // Links it to the application
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
            
            responseList.add(dto);
        }

        return responseList;
    }
    public List<ApplicationResponseDto> getStudentApplications1(Long accountId) {
        List<ApplicationTracker> trackers = trackerRepository.findByUserAccount_AccountId(accountId);
        List<ApplicationResponseDto> responseList = new ArrayList<>();

        for (ApplicationTracker tracker : trackers) {
            ApplicationResponseDto dto = new ApplicationResponseDto();
            dto.setTrackerId(tracker.getTrackerId());
            dto.setScholarshipTitle(tracker.getScholarship().getTitle());
            dto.setProvider(tracker.getScholarship().getProvider());
            dto.setStatus(tracker.getStatus());
            dto.setSubmissionDate(tracker.getSubmissionDate()); // or getApplicationDate() depending on what you named it!
            
            responseList.add(dto);
        }

        return responseList;
    }
    @Autowired private ChecklistItemRepository checklistRepository;
    public void markChecklistItemSubmitted(Long checklistItemId) {
        ChecklistItem item = checklistRepository.findById(checklistItemId)
                .orElseThrow(() -> new RuntimeException("Checklist item not found"));
        
        item.setIsCompleted(true); // Flips it to true!
        checklistRepository.save(item);
    }
}
