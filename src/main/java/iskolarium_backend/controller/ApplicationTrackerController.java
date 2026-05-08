package iskolarium_backend.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    // POST endpoint at: http://localhost:8080/api/applications/apply
    @PostMapping("/apply")
    public ResponseEntity<String> applyForScholarship(@RequestBody ApplicationRequestDto dto, Principal principal) {
        try {
            String userEmail = principal.getName();
            trackerService.submitApplication(dto, userEmail);
            return ResponseEntity.ok("Successfully applied! Your application is now PENDING.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error submitting application: " + e.getMessage());
        }
    }
    

    // GET endpoint at: http://localhost:8080/api/applications
    @GetMapping
    public ResponseEntity<?> getUserApplications(Principal principal) {
        try {
            String userEmail = principal.getName();
            List<ApplicationResponseDto> applications = trackerService.getStudentApplicationsByEmail(userEmail);
            
           if (applications.isEmpty()) {
                return ResponseEntity.ok(new ArrayList<>()); // Returns [] which JS loves
            }
            
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching applications: " + e.getMessage());
        }
    }
    // PUT endpoint at: http://localhost:8080/api/applications/checklist/5/check
    @PutMapping("/checklist/{itemId}")
    public ResponseEntity<?> updateChecklistStatus(@PathVariable Long itemId, @RequestParam boolean completed) {
        try {
            trackerService.updateChecklistItemStatus(itemId, completed);
            return ResponseEntity.ok("Status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating item: " + e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApplication(@PathVariable Long id) {
        try {
            trackerService.deleteApplication(id);
            return ResponseEntity.ok("Scholarship removed from dashboard.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}