package iskolarium_backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ApplicationResponseDto {
    private Long trackerId;
    private String scholarshipTitle;
    private String provider;
    private String status;
    private LocalDate submissionDate;
}