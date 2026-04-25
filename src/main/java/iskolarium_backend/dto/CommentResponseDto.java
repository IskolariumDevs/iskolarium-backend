package iskolarium_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentResponseDto {
    private Long commentId;
    private String textContent;
    private String authorName;
    private LocalDateTime timestamp;
}