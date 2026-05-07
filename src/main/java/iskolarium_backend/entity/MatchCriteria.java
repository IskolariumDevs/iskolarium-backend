package iskolarium_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    private String incomeBracket;

    // Links this criteria directly to its parent Scholarship
    @OneToOne
    @JoinColumn(name = "scholarship_id", referencedColumnName = "scholarshipId")
    @JsonIgnore
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
    @CollectionTable(name = "criteria_provinces", joinColumns = @JoinColumn(name = "criteria_id"))
    @Column(name = "province")
    private List<String> eligibleProvinces;

    public List<String> getEligibleProvinces() {
        return eligibleProvinces;
    }

    public void setEligibleProvinces(List<String> eligibleProvinces) {
        this.eligibleProvinces = eligibleProvinces;
    }




}