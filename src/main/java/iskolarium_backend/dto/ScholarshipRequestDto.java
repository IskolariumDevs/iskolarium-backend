package iskolarium_backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List; // Add this import!

@Data
public class ScholarshipRequestDto {
    private String title;
    private String provider;
    private String description;
    private String benefits;
    private String applicationLink;
    private LocalDate deadlineDate;

    // Upgraded to Lists!
    private List<String> requirements; 
    private List<String> eligibleYearLevels;

    private Double minGwa;
    private String targetUniversities;
    private String targetPrograms;
    private String eligibleCities;
}