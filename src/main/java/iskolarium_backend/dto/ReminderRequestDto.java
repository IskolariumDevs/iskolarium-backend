package iskolarium_backend.dto;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ReminderRequestDto {
    private Long accountId;
    private Long scholarshipId;
    private LocalDate notificationDate; 
}