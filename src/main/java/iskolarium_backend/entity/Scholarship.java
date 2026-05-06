package iskolarium_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "scholarships")
public class Scholarship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scholarshipId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String provider;

    // Using TEXT 
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String benefits;

    @Column(columnDefinition = "TEXT")

    private String applicationLink;

    private LocalDate deadlineDate;

    @OneToOne(mappedBy = "scholarship", cascade = CascadeType.ALL)
    private MatchCriteria matchCriteria;

    // This tells Spring Boot to create a separate table just to hold this list of strings!
    @ElementCollection
    @CollectionTable(name = "scholarship_requirements", joinColumns = @JoinColumn(name = "scholarship_id"))
    @Column(name = "requirement")
    private List<String> requirements;
}