package iskolarium_backend.service;

import iskolarium_backend.dto.UserProfileResponseDto;
import iskolarium_backend.dto.UserRegistrationDto;
import iskolarium_backend.entity.StudentProfile;
import iskolarium_backend.entity.UserAccount;
import iskolarium_backend.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    public UserAccount registerUser(UserRegistrationDto dto) {
        // 1. Create the base User Account
        UserAccount account = new UserAccount();
        account.setEmail(dto.getEmail());
        
        // save the raw string.
        account.setPasswordHash(dto.getPassword()); 
        account.setStatus("ACTIVE");

        // 2. Create the linked Student Profile
        StudentProfile profile = new StudentProfile();
        profile.setFirstName(dto.getFirstName());
        profile.setMiddleName(dto.getMiddleName());
        profile.setLastName(dto.getLastName());
        profile.setGwa(dto.getGwa());
        profile.setUniversity(dto.getUniversity());
        profile.setProgram(dto.getProgram());
        profile.setCity(dto.getCity());
        profile.setIncomeBracket(dto.getIncomeBracket());
        profile.setStrand(dto.getStrand());

        // 3. Link them together
        profile.setUserAccount(account);
        account.setStudentProfile(profile);

        // 4. Save to database
        return userAccountRepository.save(account);
    }
  

    public StudentProfile findByEmail(String email) {
        return userAccountRepository.findByEmail(email)
            .map(UserAccount::getStudentProfile) 
            .orElseThrow(() -> new RuntimeException("Student not found with email: " + email));
}
    public UserProfileResponseDto getUserProfile(Long accountId) {
        // Search the database for the ID
        Optional<UserAccount> accountOpt = userAccountRepository.findById(accountId);

        if (accountOpt.isPresent()) {
            UserAccount account = accountOpt.get();
            StudentProfile profile = account.getStudentProfile();

            // Pack the safe data into our Response DTO
            UserProfileResponseDto response = new UserProfileResponseDto();
            response.setAccountId(account.getAccountId());
            response.setEmail(account.getEmail());
            response.setStatus(account.getStatus());

            if (profile != null) {
                response.setFirstName(profile.getFirstName());
                response.setMiddleName(profile.getMiddleName());
                response.setLastName(profile.getLastName());
                response.setGwa(profile.getGwa());
                response.setUniversity(profile.getUniversity());
                response.setProgram(profile.getProgram());
                response.setCity(profile.getCity());
                response.setIncomeBracket(profile.getIncomeBracket());
                response.setStrand(profile.getStrand());
            }
            return response;
        }
        
        throw new RuntimeException("User not found with ID: " + accountId);
    }
}