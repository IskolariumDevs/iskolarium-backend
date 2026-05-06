package iskolarium_backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ScholarshipMatchDto {
    private Long scholarshipId;
    private String title;
    private String provider;
    private String description;
    private LocalDate deadlineDate;
    private Double requiredGwa;
}