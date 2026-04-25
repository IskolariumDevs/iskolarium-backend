package iskolarium_backend.repository;

import iskolarium_backend.entity.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
    
    // sorted by oldest to newest
    @Query("SELECT m FROM PrivateMessage m WHERE (m.sender.accountId = :userA AND m.receiver.accountId = :userB) OR (m.sender.accountId = :userB AND m.receiver.accountId = :userA) ORDER BY m.timestamp ASC")
    List<PrivateMessage> findConversationThread(@Param("userA") Long userA, @Param("userB") Long userB);
    
}