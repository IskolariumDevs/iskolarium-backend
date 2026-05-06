package iskolarium_backend.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageResponseDto {
    private Long messageId;
    private String senderName; // hide if anonymous
    private String content;
    private LocalDateTime timestamp;
    private Boolean isYours; // to know whether to put the chat bubble on the left or right side
}