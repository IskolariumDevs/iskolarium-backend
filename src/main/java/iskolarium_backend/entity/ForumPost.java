package iskolarium_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "forum_posts")
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String textContent;

    private String imageUrl;

    private Integer upvoteCount = 0; // Default to 0 votes

    @Column(nullable = false)
    private Boolean isAnonymous;


    @Column(nullable = false)
    private LocalDateTime timestamp;

    // THE FOREIGN KEY
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "accountId")
    private UserAccount author;

    @Column(name = "is_resolved", nullable = false, columnDefinition = "boolean default false")
    private boolean isResolved = false;

    // Setter for the resolved status
    public void setResolved(boolean resolved) {
        this.isResolved = resolved;
    }
    // Getter for the resolved status
    public boolean isResolved() {
        return this.isResolved;
    }
    
}