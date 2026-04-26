package iskolarium_backend.controller;

import iskolarium_backend.dto.LoginRequestDto;
import iskolarium_backend.entity.UserAccount;
import iskolarium_backend.repository.UserAccountRepository;
import iskolarium_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserAccountRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // POST: http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequest) {
        
        // look for the user in the database
        Optional<UserAccount> userOpt = userRepository.findByEmail(loginRequest.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Error: Email not found.");
        }

        UserAccount user = userOpt.get();

        // check the password using bcrypt
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(401).body("Error: Incorrect password.");
        }

        // password matches then print the keycard
        String token = jwtUtil.generateToken(user.getEmail());

        // hand the token back to the frontend
        return ResponseEntity.ok(token);
    }
}