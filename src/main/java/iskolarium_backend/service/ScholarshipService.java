package iskolarium_backend.service;

import iskolarium_backend.dto.ScholarshipRequestDto;
import iskolarium_backend.entity.MatchCriteria;
import iskolarium_backend.entity.Scholarship;
import iskolarium_backend.repository.ScholarshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // Added this import
import java.util.List;      // Added this import

@Service
public class ScholarshipService {

    @Autowired
    private ScholarshipRepository scholarshipRepository;

    public Scholarship createScholarship(ScholarshipRequestDto dto) {
        Scholarship scholarship = new Scholarship();
        scholarship.setTitle(dto.getTitle());
        scholarship.setProvider(dto.getProvider());
        scholarship.setDescription(dto.getDescription());
        scholarship.setBenefits(dto.getBenefits());
        scholarship.setRequirements(dto.getRequirements());
        scholarship.setApplicationLink(dto.getApplicationLink());
        scholarship.setDeadlineDate(dto.getDeadlineDate());

        MatchCriteria criteria = new MatchCriteria();
        criteria.setMinGwa(dto.getMinGwa());
        criteria.setTargetUniversities(dto.getTargetUniversities());
        criteria.setTargetPrograms(dto.getTargetPrograms());
        criteria.setEligibleCities(dto.getEligibleCities());
        criteria.setEligibleYearLevels(dto.getEligibleYearLevels()); 

        criteria.setScholarship(scholarship);
        scholarship.setMatchCriteria(criteria);

        return scholarshipRepository.save(scholarship);
    }

    public List<Scholarship> searchScholarships(Double gwa, String program, String university, String province, String city, String incomeBracket, String strand) {
        List<Scholarship> allScholarships = scholarshipRepository.findAll();
        List<Scholarship> matchingScholarships = new ArrayList<>();

        for (Scholarship scholarship : allScholarships) {
            MatchCriteria criteria = scholarship.getMatchCriteria();
            if (criteria == null) continue;

            boolean isMatch = true;

            // 1. GWA (Lower is better)
            if (gwa != null && criteria.getMinGwa() != null) {
                if (gwa > criteria.getMinGwa()) isMatch = false;
            }

            // 2. Program
            if (program != null && !program.trim().isEmpty() && criteria.getTargetPrograms() != null) {
                boolean isAll = criteria.getTargetPrograms().equalsIgnoreCase("ALL");
                boolean matches = criteria.getTargetPrograms().toLowerCase().contains(program.toLowerCase());
                if (!isAll && !matches) isMatch = false;
            }

            // 3. University
            if (university != null && !university.trim().isEmpty() && criteria.getTargetUniversities() != null) {
                boolean isAll = criteria.getTargetUniversities().equalsIgnoreCase("ALL");
                boolean matches = criteria.getTargetUniversities().toLowerCase().contains(university.toLowerCase());
                if (!isAll && !matches) isMatch = false;
            }

            // 4. City
            if (city != null && !city.trim().isEmpty() && criteria.getEligibleCities() != null) {
                // .trim() removes any accidental spaces from the database like "ALL "
                String dbCities = criteria.getEligibleCities().trim(); 
                String userCity = city.trim();

                boolean isAll = dbCities.equalsIgnoreCase("ALL");
                boolean matchesExact = dbCities.toLowerCase().contains(userCity.toLowerCase());
                
                if (!isAll && !matchesExact) {
                    // 🚨 DEVELOPER CHEAT CODE: Print to terminal so you can see why it failed!
                    System.out.println("❌ FAILED ON CITY: User selected '" + userCity + "', but DB requires '" + dbCities + "'");
                    isMatch = false;
                }
            }

            // 5. Income Bracket (Using the Ranker)
            if (incomeBracket != null && !incomeBracket.trim().isEmpty() && criteria.getIncomeBracket() != null) {
                boolean isAll = criteria.getIncomeBracket().equalsIgnoreCase("ALL");
                if (!isAll && getIncomeLevel(incomeBracket) > getIncomeLevel(criteria.getIncomeBracket())) {
                    isMatch = false;
                }
            }

            if (isMatch) matchingScholarships.add(scholarship);
        }
        return matchingScholarships;
    }

    private int getIncomeLevel(String bracket) {
        if (bracket == null || bracket.equalsIgnoreCase("ALL")) return 99;
        String clean = bracket.toLowerCase().replaceAll("[^0-9a-z]", "");
        
        if (clean.contains("below") && clean.contains("100")) return 1;
        if (clean.contains("100") && clean.contains("250")) return 2;
        if (clean.contains("250") && clean.contains("400")) return 3;
        if (clean.contains("400") && clean.contains("600")) return 4;
        if (clean.contains("600") && clean.contains("1000")) return 5;
        if (clean.contains("above") && clean.contains("1000")) return 6;
        
        return 99;
    }
}