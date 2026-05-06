package iskolarium_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

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

    // We can store these as comma-separated lists for now 
    private String targetUniversities;
    
    private String targetPrograms;
    
    private String eligibleCities;

    // Links this criteria directly to its parent Scholarship
    @OneToOne
    @JoinColumn(name = "scholarship_id", referencedColumnName = "scholarshipId")
    private Scholarship scholarship;

    @ElementCollection
    @CollectionTable(name = "criteria_year_levels", joinColumns = @JoinColumn(name = "criteria_id"))
    @Column(name = "year_level")
    private List<String> eligibleYearLevels;

    @ElementCollection
    @CollectionTable(name = "criteria_strands", joinColumns = @JoinColumn(name = "criteria_id"))
    @Column(name = "strand")
    private List<String> eligibleStrands;

    @ElementCollection
    @CollectionTable(name = "criteria_strands", joinColumns = @JoinColumn(name = "criteria_id"))
    @Column(name = "strand")
    private List<String> strands;
    public List<String> getStrands() { return strands; }
    public void setStrands(List<String> strands) { this.strands = strands; }
}