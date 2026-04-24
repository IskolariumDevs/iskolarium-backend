package iskolarium_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "application_trackers")
public class ApplicationTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackerId;

    @Column(nullable = false)
    private String status; // e.g., "IN_PROGRESS", "SUBMITTED"

    private Integer progressPercentage; // e.g., 50 for 50%

    private LocalDate dateStarted;

    // Many trackers can belong to one User Account [cite: 79]
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    private UserAccount userAccount;

    // Many trackers can point to one Scholarship [cite: 79]
    @ManyToOne
    @JoinColumn(name = "scholarship_id", referencedColumnName = "scholarshipId")
    private Scholarship scholarship;

    // One Tracker has Many Checklist Items
    @OneToMany(mappedBy = "tracker", cascade = CascadeType.ALL)
    private List<ChecklistItem> checklistItems;
}