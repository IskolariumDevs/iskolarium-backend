package iskolarium_backend.dto;

import lombok.Data;

@Data
public class UserProfileResponseDto {
    private Long accountId;
    private String email;
    private String status;
    
    // Profile details
    private String firstName;
    private String middleName;
    private String lastName;
    private Double gwa;
    private String university;
    private String program;
    private String city;
    private String incomeBracket;
    private String strand;
}