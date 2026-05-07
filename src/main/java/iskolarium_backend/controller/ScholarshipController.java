package iskolarium_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iskolarium_backend.dto.ScholarshipRequestDto;
import iskolarium_backend.entity.Scholarship;
import iskolarium_backend.entity.StudentProfile;
import iskolarium_backend.repository.ScholarshipRepository;
import iskolarium_backend.service.ScholarshipService;
import iskolarium_backend.service.UserAccountService;

@RestController
@RequestMapping("/api/scholarships")
@CrossOrigin(origins = "*")

public class ScholarshipController {

@Autowired
    private ScholarshipService scholarshipService;

    @Autowired
    private UserAccountService userAccountService; 

    @Autowired
    private ScholarshipRepository scholarshipRepository;

    // POST endpoint at: http://localhost:8080/api/scholarships
    @PostMapping
    public ResponseEntity<String> createScholarship(@RequestBody ScholarshipRequestDto dto) {
        try {
            scholarshipService.createScholarship(dto);
            return ResponseEntity.ok("Scholarship and Criteria posted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error posting scholarship: " + e.getMessage());
        }
    }

 @GetMapping("/recommended")
    public ResponseEntity<List<Scholarship>> getRecommendations(@RequestParam String email) {
        // Updated to use userAccountService
        StudentProfile profile = userAccountService.findByEmail(email);
        
        List<Scholarship> matches = scholarshipRepository.findRecommended(
            profile.getGwa(),
            profile.getCity(),
            profile.getUniversity(),
            profile.getStrand()
        );
        
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Scholarship>> searchScholarships(
            @RequestParam(required = false) Double gwa,
            @RequestParam(required = false) String program,
            @RequestParam(required = false) String university,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String incomeBracket,
            @RequestParam(required = false) String strand) {
        
        List<Scholarship> matches = scholarshipRepository.findByFilters(
            gwa, program, university, city, province, incomeBracket, strand
        );
        
        return ResponseEntity.ok(matches);
    }
}
