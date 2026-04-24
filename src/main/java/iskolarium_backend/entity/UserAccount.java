package iskolarium_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // Lombok annotation to auto-generate getters and setters
@Table(name = "user_accounts")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String status; // e.g., "ACTIVE", "PENDING"
    
    // One UserAccount has One StudentProfile
    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private StudentProfile studentProfile;
}