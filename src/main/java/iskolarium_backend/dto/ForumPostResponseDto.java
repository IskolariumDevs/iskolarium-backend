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
    
    public String getAuthorEmail() {
        return authorEmail;
    }
    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }
    public Long getAuthorId() {
        return authorId;
    }
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
    public boolean getIsResolved() {
        return isResolved;
    }
    public void setIsResolved(boolean isResolved) {
        this.isResolved = isResolved;
    }
    private LocalDateTime timestamp;
}