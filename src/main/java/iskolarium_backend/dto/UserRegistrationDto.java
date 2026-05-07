package iskolarium_backend.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    // Account details
    private String email;
    private String password;
    
    // Student Profile details - Name separated
    private String firstName;
    private String middleName;
    private String lastName;
    
    private Double gwa;
    private String university;
    private String program;
    private String city;
    private String province;
    private String incomeBracket;
    private String strand; // For incoming freshmen
}