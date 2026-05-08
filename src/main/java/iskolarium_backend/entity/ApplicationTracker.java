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

    
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "scholarship_id", referencedColumnName = "scholarshipId")
    private Scholarship scholarship;


    @Column(name = "submission_date")
    private LocalDate submissionDate;

    @OneToMany(mappedBy = "tracker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistItem> checklistItems;
    
    }
