package iskolarium_backend.dto;

import lombok.Data;

@Data
public class ForumPostRequestDto {
    private Long accountId;
    private String textContent; // Matches your entity!
    private Boolean isAnonymous; // Added this since your entity requires it!
}