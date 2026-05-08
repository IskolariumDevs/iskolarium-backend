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
            
            // Skip if no criteria exists
            if (criteria == null) continue;

            boolean isMatch = true;

            // RULE 1: The GWA Check (PH System: 1.0 is highest, 3.0 is passing)
            if (criteria.getMinGwa() != null && profile.getGwa() != null) {
                if (profile.getGwa() > criteria.getMinGwa()) {
                    isMatch = false; 
                }
            }

            if (criteria.getEligibleCities() != null && profile.getCity() != null) {
                String dbCities = criteria.getEligibleCities().trim();
                String userCity = profile.getCity().trim();

                boolean isAll = dbCities.equalsIgnoreCase("ALL");
                boolean matchesCity = dbCities.toLowerCase().contains(userCity.toLowerCase());
                
                if (!isAll && !matchesCity) {
                    isMatch = false; 
                }
            }

            // RULE 3: The University Check (Bulletproof)
            if (criteria.getTargetUniversities() != null && profile.getUniversity() != null) {
                String dbUnivs = criteria.getTargetUniversities().trim();
                String userUniv = profile.getUniversity().trim();

                boolean isAll = dbUnivs.equalsIgnoreCase("ALL");
                boolean matchesUniv = dbUnivs.toLowerCase().contains(userUniv.toLowerCase());
                
                if (!isAll && !matchesUniv) {
                    isMatch = false; 
                }
            }

            // RULE 4: The Program Check (Bulletproof)
            if (criteria.getTargetPrograms() != null && profile.getProgram() != null) {
                String dbPrograms = criteria.getTargetPrograms().trim();
                String userProgram = profile.getProgram().trim();

                boolean isAll = dbPrograms.equalsIgnoreCase("ALL");
                boolean matchesProgram = dbPrograms.toLowerCase().contains(userProgram.toLowerCase());
                
                if (!isAll && !matchesProgram) {
                    isMatch = false;
                }
            }

            // RULE 5: Income Bracket Check
            if (criteria.getIncomeBracket() != null && profile.getIncomeBracket() != null) {
                boolean isAll = criteria.getIncomeBracket().equalsIgnoreCase("ALL");
                
                // Get the numerical levels
                int userIncomeLevel = getIncomeLevel(profile.getIncomeBracket());
                int requiredIncomeLevel = getIncomeLevel(criteria.getIncomeBracket());
                
                // If the user's income level is HIGHER than the maximum allowed by the scholarship, they fail.
                // (e.g., User is Level 3, but Scholarship requires Level 2 or below)
                if (!isAll && userIncomeLevel > requiredIncomeLevel) {
                    isMatch = false;
                }
            }

            // If the scholarship survived all the rules, it's a match!
            if (isMatch) {
                ScholarshipMatchDto dto = new ScholarshipMatchDto();
                dto.setScholarshipId(scholarship.getId()); // Using getId() to match standard naming
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
    private int getIncomeLevel(String bracket) {
        if (bracket == null || bracket.equalsIgnoreCase("ALL")) return 99;

        // Clean the string: "₱250,001 - ₱400,000" becomes "250001400000"
        String clean = bracket.toLowerCase().replaceAll("[^0-9a-z]", "");

        // Map directly to your 6 dropdown options
        if (clean.contains("below") && clean.contains("100")) return 1;
        if (clean.contains("100") && clean.contains("250")) return 2;
        if (clean.contains("250") && clean.contains("400")) return 3;
        if (clean.contains("400") && clean.contains("600")) return 4;
        if (clean.contains("600") && clean.contains("1000")) return 5;
        if (clean.contains("above") && clean.contains("1000")) return 6;

        return 99; // Fallback for anything else
    }
}