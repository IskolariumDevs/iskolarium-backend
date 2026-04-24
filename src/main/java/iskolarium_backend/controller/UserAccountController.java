package iskolarium_backend.controller;

import iskolarium_backend.dto.UserProfileResponseDto;
import iskolarium_backend.dto.UserRegistrationDto;
import iskolarium_backend.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") 
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    // POST endpoint at: http://localhost:8080/api/users/register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDto dto) {
        try {
            userAccountService.registerUser(dto);
            return ResponseEntity.ok("User and Student Profile created successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error registering user: " + e.getMessage());
        }
        
    }
    // This creates a GET endpoint at: http://localhost:8080/api/users/1
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        try {
            // Ask the service to find the user
            UserProfileResponseDto profile = userAccountService.getUserProfile(id);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}