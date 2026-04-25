package iskolarium_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    // Links this comment to a specific post
    @ManyToOne
    @JoinColumn(name = "post_id")
    private ForumPost forumPost;

    // Links this comment to the user who wrote it
    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserAccount author;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String textContent;

    private LocalDateTime timestamp;
}