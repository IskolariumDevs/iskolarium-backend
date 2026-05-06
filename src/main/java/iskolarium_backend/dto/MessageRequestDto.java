package iskolarium_backend.dto;
import lombok.Data;

@Data
public class MessageRequestDto {
    private Long senderId;
    private Long receiverId;
    private String content;
    private Boolean isAnonymous;
}