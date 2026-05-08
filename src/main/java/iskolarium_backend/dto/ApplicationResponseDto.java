package iskolarium_backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ApplicationResponseDto {
    private Long trackerId;
    private String scholarshipTitle;
    private String provider;
    private String status;
    private LocalDate submissionDate;
    
    // New fields for enhanced dashboard
    private List<ChecklistItemDto> requirements;
    private String benefits;
    private String applicationLink;
    private LocalDate deadlineDate;
    
    // Deadline warning fields
    private Long daysRemaining;
    private Boolean isPriorityWarning;
}