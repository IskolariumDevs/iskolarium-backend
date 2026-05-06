package iskolarium_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "student_profiles")
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false)
    private Double gwa;

    @Column(nullable = false)
    private String university;

    @Column(nullable = false)
    private String program;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String incomeBracket;

    // Links this profile back to the exact UserAccount
    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    private UserAccount userAccount;
}
