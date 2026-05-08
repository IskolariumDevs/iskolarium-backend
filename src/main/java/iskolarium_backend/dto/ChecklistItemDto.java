package iskolarium_backend.dto;

import lombok.Data;

@Data
public class ChecklistItemDto {
    private Long id;
    private String requirementName;
    private Boolean completed;
}