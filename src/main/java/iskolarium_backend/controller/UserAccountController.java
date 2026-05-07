package iskolarium_backend.controller;

import iskolarium_backend.dto.UserProfileResponseDto;
import iskolarium_backend.dto.UserRegistrationDto;
import iskolarium_backend.entity.StudentProfile;
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

    // GET endpoint at: http://localhost:8080/api/users/profile?email=user@example.com
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfileByEmail(@RequestParam String email) {
        try {
            StudentProfile profile = userAccountService.findByEmail(email);
            UserProfileResponseDto response = new UserProfileResponseDto();
            response.setFirstName(profile.getFirstName());
            response.setMiddleName(profile.getMiddleName());
            response.setLastName(profile.getLastName());
            response.setGwa(profile.getGwa());
            response.setUniversity(profile.getUniversity());
            response.setProgram(profile.getProgram());
            response.setCity(profile.getCity());
            response.setProvince(profile.getProvince());
            response.setIncomeBracket(profile.getIncomeBracket());
            response.setStrand(profile.getStrand());
            response.setEmail(email);
            if (profile.getUserAccount() != null) {
                response.setAccountId(profile.getUserAccount().getAccountId());
                response.setStatus(profile.getUserAccount().getStatus());
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}