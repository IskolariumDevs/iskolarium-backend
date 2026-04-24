package iskolarium_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

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

    // Using TEXT so you can store long paragraphs without the database crashing
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String benefits;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    private String applicationLink;

    // LocalDate is perfect for handling standard YYYY-MM-DD deadlines
    private LocalDate deadlineDate;

    // One Scholarship has One set of MatchCriteria
    @OneToOne(mappedBy = "scholarship", cascade = CascadeType.ALL)
    private MatchCriteria matchCriteria;
}