package iskolarium_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApplicationRequestDto {
    
    @JsonProperty("scholarshipId") // This forces it to match your JS key exactly
    private Long scholarshipId;
}