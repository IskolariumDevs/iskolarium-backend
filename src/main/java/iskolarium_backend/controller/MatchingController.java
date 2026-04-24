package iskolarium_backend.controller;

import iskolarium_backend.dto.ScholarshipMatchDto;
import iskolarium_backend.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
@CrossOrigin(origins = "*")
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    // Creates a GET endpoint at: http://localhost:8080/api/match/1
    @GetMapping("/{accountId}")
    public ResponseEntity<?> getStudentMatches(@PathVariable Long accountId) {
        try {
            List<ScholarshipMatchDto> matches = matchingService.getMatchesForStudent(accountId);
            
            // If the list is empty, we can let the frontend know
            if (matches.isEmpty()) {
                return ResponseEntity.ok("No eligible scholarships found for this profile.");
            }
            
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error finding matches: " + e.getMessage());
        }
    }
}