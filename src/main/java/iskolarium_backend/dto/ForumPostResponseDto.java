package iskolarium_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ForumPostResponseDto {
    private Long postId;
    private String textContent;
    private String authorName;
    private Boolean isAnonymous;
    private Integer upvoteCount;
    private Boolean isResolved;
    private LocalDateTime timestamp;
}