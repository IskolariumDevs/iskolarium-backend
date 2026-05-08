package iskolarium_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ForumPostResponseDto {
    private Long postId;
    private String textContent;
    private String authorName;
    private Boolean isResolved;
    private String authorEmail;
    private Long authorId;
    private LocalDateTime timestamp;
}