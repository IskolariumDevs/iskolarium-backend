package iskolarium_backend.controller;

import iskolarium_backend.dto.ScholarshipRequestDto;
import iskolarium_backend.service.ScholarshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scholarships")
@CrossOrigin(origins = "*")
public class ScholarshipController {

    @Autowired
    private ScholarshipService scholarshipService;

    // Creates a POST endpoint at: http://localhost:8080/api/scholarships
    @PostMapping
    public ResponseEntity<String> createScholarship(@RequestBody ScholarshipRequestDto dto) {
        try {
            scholarshipService.createScholarship(dto);
            return ResponseEntity.ok("Scholarship and Criteria posted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error posting scholarship: " + e.getMessage());
        }
    }
}