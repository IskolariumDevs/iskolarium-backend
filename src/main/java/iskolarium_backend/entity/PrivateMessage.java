package iskolarium_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "private_messages")
public class PrivateMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    // The person sending the message
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserAccount sender;

    // The person receiving the message
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private UserAccount receiver;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private Boolean isAnonymous;
    
    private LocalDateTime timestamp;
}