package iskolarium_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "checklist_items")
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(nullable = false)
    private String requirementName;

    @Column(nullable = false)
    private Boolean isCompleted;

    // Many Checklist Items belong to One Tracker 
    @ManyToOne
    @JoinColumn(name = "tracker_id", referencedColumnName = "trackerId")
    private ApplicationTracker tracker;
}