package iskolarium_backend.service;

import iskolarium_backend.dto.ScholarshipRequestDto;
import iskolarium_backend.entity.MatchCriteria;
import iskolarium_backend.entity.Scholarship;
import iskolarium_backend.repository.ScholarshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScholarshipService {

    @Autowired
    private ScholarshipRepository scholarshipRepository;

    public Scholarship createScholarship(ScholarshipRequestDto dto) {
        // 1. Build the Scholarship
        Scholarship scholarship = new Scholarship();
        scholarship.setTitle(dto.getTitle());
        scholarship.setProvider(dto.getProvider());
        scholarship.setDescription(dto.getDescription());
        scholarship.setBenefits(dto.getBenefits());
        scholarship.setRequirements(dto.getRequirements());
        scholarship.setApplicationLink(dto.getApplicationLink());
        scholarship.setDeadlineDate(dto.getDeadlineDate());

        // 2. Build the Match Criteria
        MatchCriteria criteria = new MatchCriteria();
        criteria.setMinGwa(dto.getMinGwa());
        criteria.setTargetUniversities(dto.getTargetUniversities());
        criteria.setTargetPrograms(dto.getTargetPrograms());
        criteria.setEligibleCities(dto.getEligibleCities());

        // 3. Link them together
        criteria.setScholarship(scholarship);
        scholarship.setMatchCriteria(criteria);

        scholarship.setRequirements(dto.getRequirements()); // This now saves a List!

        criteria.setEligibleYearLevels(dto.getEligibleYearLevels()); // Add this new line!
        // 4. Save to database (CascadeType.ALL will save the criteria automatically!)
        return scholarshipRepository.save(scholarship);
    

    }
}