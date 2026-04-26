package iskolarium_backend.controller;

import iskolarium_backend.dto.UserProfileResponseDto;
import iskolarium_backend.dto.UserRegistrationDto;
import iskolarium_backend.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") 
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // POST endpoint at: http://localhost:8080/api/users/register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDto dto) {
        try {
            // grab the plain text password and scramble it
            String scrambledPassword = passwordEncoder.encode(dto.getPassword());
            
            // put the scrambled password back into the envelope
            dto.setPassword(scrambledPassword); 
            
            // hand it to the service to save
            userAccountService.registerUser(dto);
            
            return ResponseEntity.ok("User and Student Profile created successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error registering user: " + e.getMessage());
        }
    }
    // GET endpoint at: http://localhost:8080/api/users/1
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        try {
            // ask the service to find the user
            UserProfileResponseDto profile = userAccountService.getUserProfile(id);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}