package iskolarium_backend.repository;

import iskolarium_backend.entity.Comment;
import iskolarium_backend.entity.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Fetches all comments for a specific post, sorted oldest to newest
    List<Comment> findAllByForumPostOrderByTimestampAsc(ForumPost forumPost);
}