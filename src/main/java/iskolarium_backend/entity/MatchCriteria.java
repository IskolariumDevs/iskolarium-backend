package iskolarium_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "match_criteria")
public class MatchCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long criteriaId;

    // The required GWA to even apply for this scholarship
    @Column(nullable = false)
    private Double minGwa;

    // We can store these as comma-separated lists for now (e.g., "Batangas State University, UP Diliman")
    private String targetUniversities;
    
    private String targetPrograms;
    
    private String eligibleCities;

    // Links this criteria directly to its parent Scholarship
    @OneToOne
    @JoinColumn(name = "scholarship_id", referencedColumnName = "scholarshipId")
    private Scholarship scholarship;
}