package iskolarium_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "reminders")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reminderId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private UserAccount userAccount;

    @ManyToOne 
    @JoinColumn(name = "scholarship_id")
    private Scholarship scholarship; 

    private LocalDate notificationDate;
    
    private Boolean isSent = false; // Default to false when created
}