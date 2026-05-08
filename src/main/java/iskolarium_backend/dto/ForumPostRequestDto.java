package iskolarium_backend.dto;

import lombok.Data;

@Data
public class ForumPostRequestDto {
    private String email;
    private String textContent;
    private boolean isAnonymous;
}