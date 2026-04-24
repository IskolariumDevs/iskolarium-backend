package iskolarium_backend.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    // Account details
    private String email;
    private String password;
    
    // Student Profile details
    private Double gwa;
    private String university;
    private String program;
    private String city;
    private String incomeBracket;
}