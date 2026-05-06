package iskolarium_backend.controller;

import iskolarium_backend.dto.CommentRequestDto;
import iskolarium_backend.dto.CommentResponseDto;
import iskolarium_backend.dto.ForumPostRequestDto;
import iskolarium_backend.dto.ForumPostResponseDto;
import iskolarium_backend.entity.Comment;
import iskolarium_backend.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forum")
@CrossOrigin(origins = "*") // Allows your frontend to talk to this backend
public class ForumController {

    @Autowired
    private ForumService forumService;

    // Endpoint to create a post
    // POST request to: http://localhost:8080/api/forum/posts
    @PostMapping("/posts")
    public ResponseEntity<String> createPost(@RequestBody ForumPostRequestDto dto) {
        try {
            forumService.createPost(dto);
            return ResponseEntity.ok("Success! Your question is now live on the forum.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating post: " + e.getMessage());
        }
    }

    // Endpoint to get all posts
    // GET request to: http://localhost:8080/api/forum/posts
    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts() {
        try {
            List<ForumPostResponseDto> posts = forumService.getAllPosts();
            
            if (posts.isEmpty()) {
                return ResponseEntity.ok("The forum is currently empty. Be the first to ask a question!");
            }
            
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching posts: " + e.getMessage());
        }
    }
    // Endpoint to upvote
    // PUT request to: http://localhost:8080/api/forum/posts/1/upvote
    @PutMapping("/posts/{postId}/upvote")
    public ResponseEntity<String> upvotePost(@PathVariable Long postId) {
        try {
            forumService.upvotePost(postId);
            return ResponseEntity.ok("Post upvoted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error upvoting: " + e.getMessage());
        }
    }
    // Endpoint to add comment
    // POST request to: http://localhost:8080/api/forum/posts/1/comments
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<String> addComment(@PathVariable Long postId, @RequestBody CommentRequestDto dto) {
        try {
            forumService.addComment(postId, dto);
            return ResponseEntity.ok("Comment added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding comment: " + e.getMessage());
        }
    }
    // Endpoint to get comments in a post
    // GET request to: http://localhost:8080/api/forum/posts/1/comments
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<?> getComments(@PathVariable Long postId) {
        try {
            // Updated to use the DTO!
            List<CommentResponseDto> comments = forumService.getCommentsForPost(postId);
            
            if (comments.isEmpty()) {
                return ResponseEntity.ok("No comments yet. Be the first to reply!");
            }
            
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching comments: " + e.getMessage());
        }
    }
    // endpoint to mark as resolved
    // PUT request to: http://localhost:8080/api/forum/posts/1/resolve?accountId=1
    @PutMapping("/posts/{postId}/resolve")
    public ResponseEntity<String> resolvePost(@PathVariable Long postId, @RequestParam Long accountId) {
        try {
            forumService.markPostAsResolved(postId, accountId);
            return ResponseEntity.ok("Success! This forum thread is now marked as resolved.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}