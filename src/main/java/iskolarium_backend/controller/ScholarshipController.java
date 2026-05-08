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

import iskolarium_backend.dto.ScholarshipMatchDto;
import iskolarium_backend.dto.ScholarshipRequestDto;
import iskolarium_backend.entity.Scholarship;
import iskolarium_backend.entity.UserAccount;
import iskolarium_backend.repository.ScholarshipRepository;
import iskolarium_backend.repository.UserAccountRepository;
import iskolarium_backend.service.MatchingService;
import iskolarium_backend.service.ScholarshipService;
import iskolarium_backend.service.UserAccountService;

@RestController
@RequestMapping("/api/scholarships")
@CrossOrigin(origins = "*")
public class ScholarshipController {

    // ==========================================
    // DEPENDENCIES (All grouped at the top!)
    // ==========================================
    @Autowired
    private ScholarshipService scholarshipService;

    @Autowired
    private UserAccountService userAccountService; 

    @Autowired
    private ScholarshipRepository scholarshipRepository;

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private UserAccountRepository userAccountRepository;


    // ==========================================
    // ENDPOINTS
    // ==========================================

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

    // GET endpoint for Automatic Profile Matching
    @GetMapping("/recommended")
    public ResponseEntity<?> getRecommendedScholarships(@RequestParam String email) {
        try {
            // 1. Find the user ID based on Saiki's email
            UserAccount user = userAccountRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 2. Send Saiki through our Bulletproof Java Gauntlet!
            List<ScholarshipMatchDto> recommendedList = matchingService.getMatchesForStudent(user.getAccountId());

            return ResponseEntity.ok(recommendedList);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching recommendations: " + e.getMessage());
        }
    }

    // GET endpoint for Manual Search Dropdowns
    @GetMapping("/search")
    public ResponseEntity<List<Scholarship>> searchScholarships(
            @RequestParam(required = false) Double gwa,
            @RequestParam(required = false) String program,
            @RequestParam(required = false) String university,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String incomeBracket,
            @RequestParam(required = false) String strand) {
        
        // Pass everything to our newly upgraded Service method
        List<Scholarship> results = scholarshipService.searchScholarships(
                gwa, program, university, province, city, incomeBracket, strand);
                
        return ResponseEntity.ok(results);
    }
}