package iskolarium_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iskolarium_backend.dto.ApplicationRequestDto;
import iskolarium_backend.dto.ApplicationResponseDto;
import iskolarium_backend.service.ApplicationTrackerService;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationTrackerController {

    @Autowired
    private ApplicationTrackerService trackerService;

    // Creates a POST endpoint at: http://localhost:8080/api/applications/apply
    @PostMapping("/apply")
    public ResponseEntity<String> applyForScholarship(@RequestBody ApplicationRequestDto dto) {
        try {
            trackerService.submitApplication(dto);
            return ResponseEntity.ok("Successfully applied! Your application is now PENDING.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error submitting application: " + e.getMessage());
        }
    }
    // Make sure to import java.util.List; at the top!

    // Creates a GET endpoint at: http://localhost:8080/api/applications/user/1
    @GetMapping("/user/{accountId}")
    public ResponseEntity<?> getUserApplications(@PathVariable Long accountId) {
        try {
            List<ApplicationResponseDto> applications = trackerService.getStudentApplications(accountId);
            
            if (applications.isEmpty()) {
                return ResponseEntity.ok("You haven't applied to any scholarships yet.");
            }
            
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching applications: " + e.getMessage());
        }
        
    }
    // Creates a PUT endpoint at: http://localhost:8080/api/applications/checklist/5/check
    @PutMapping("/checklist/{itemId}/check")
    public ResponseEntity<String> checkOffRequirement(@PathVariable Long itemId) {
        try {
            // We can add a quick method in the service to handle this, or do it right here if you have the repository!
            // Let's assume you add a method in your trackerService for this:
            trackerService.markChecklistItemSubmitted(itemId);
            return ResponseEntity.ok("Requirement marked as submitted!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating checklist: " + e.getMessage());
        }
    }
}