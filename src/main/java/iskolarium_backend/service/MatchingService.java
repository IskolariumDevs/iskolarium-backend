package iskolarium_backend.service;

import iskolarium_backend.dto.ScholarshipMatchDto;
import iskolarium_backend.entity.MatchCriteria;
import iskolarium_backend.entity.Scholarship;
import iskolarium_backend.entity.StudentProfile;
import iskolarium_backend.entity.UserAccount;
import iskolarium_backend.repository.ScholarshipRepository;
import iskolarium_backend.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchingService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private ScholarshipRepository scholarshipRepository;

    public List<ScholarshipMatchDto> getMatchesForStudent(Long accountId) {
        // 1. Fetch the Student
        UserAccount account = userAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        StudentProfile profile = account.getStudentProfile();

        // 2. Fetch ALL Scholarships
        List<Scholarship> allScholarships = scholarshipRepository.findAll();
        List<ScholarshipMatchDto> matchedList = new ArrayList<>();

        // 3. THE GAUNTLET (The Algorithm)
        for (Scholarship scholarship : allScholarships) {
            MatchCriteria criteria = scholarship.getMatchCriteria();
            boolean isMatch = true;

            // RULE 1: The GWA Check (PH System: 1.0 is highest, 3.0 is passing)
            if (profile.getGwa() > criteria.getMinGwa()) {
                isMatch = false; // Failed! GWA is too low.
            }

            // RULE 2: The City Check
            if (!criteria.getEligibleCities().equalsIgnoreCase("All") &&
                !criteria.getEligibleCities().contains(profile.getCity())) {
                isMatch = false; // Failed! Doesn't live in the right city.
            }

            // RULE 3: The University Check
            if (!criteria.getTargetUniversities().equalsIgnoreCase("All") &&
                !criteria.getTargetUniversities().contains(profile.getUniversity())) {
                isMatch = false; // Failed! Doesn't go to the right school.
            }

            // If the scholarship survived all the rules, it's a match!
            if (isMatch) {
                ScholarshipMatchDto dto = new ScholarshipMatchDto();
                dto.setScholarshipId(scholarship.getScholarshipId());
                dto.setTitle(scholarship.getTitle());
                dto.setProvider(scholarship.getProvider());
                dto.setDescription(scholarship.getDescription());
                dto.setDeadlineDate(scholarship.getDeadlineDate());
                dto.setRequiredGwa(criteria.getMinGwa());
                
                matchedList.add(dto);
            }
        }

        return matchedList;
    }
}