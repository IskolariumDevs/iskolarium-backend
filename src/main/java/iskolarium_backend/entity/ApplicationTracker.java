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
    private String status; 

    private Integer progressPercentage;

    private LocalDate dateStarted;

    
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    private UserAccount userAccount;

    
    @ManyToOne
    @JoinColumn(name = "scholarship_id", referencedColumnName = "scholarshipId")
    private Scholarship scholarship;


    @OneToMany(mappedBy = "tracker", cascade = CascadeType.ALL)
    private List<ChecklistItem> checklistItems;

    @Column(name = "submission_date")
    private LocalDate submissionDate;
    
    }
