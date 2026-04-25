package iskolarium_backend.dto;

import lombok.Data;

@Data
public class CommentRequestDto {
    private Long accountId;
    private String textContent;
}