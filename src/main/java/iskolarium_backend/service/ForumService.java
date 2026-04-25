package iskolarium_backend.service;

import iskolarium_backend.dto.CommentRequestDto;
import iskolarium_backend.dto.CommentResponseDto;
import iskolarium_backend.dto.ForumPostRequestDto;
import iskolarium_backend.dto.ForumPostResponseDto;
import iskolarium_backend.entity.Comment;
import iskolarium_backend.entity.ForumPost;
import iskolarium_backend.entity.UserAccount;
import iskolarium_backend.repository.CommentRepository;
import iskolarium_backend.repository.ForumPostRepository;
import iskolarium_backend.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ForumService {

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired private CommentRepository commentRepository;

    // --- 1. METHOD TO CREATE A POST ---
    public ForumPost createPost(ForumPostRequestDto dto) {
        UserAccount postAuthor = userAccountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ForumPost post = new ForumPost();
        post.setAuthor(postAuthor);               // Perfectly matches your 'author' variable
        post.setTextContent(dto.getTextContent());// Perfectly matches your 'textContent' variable
        post.setTimestamp(LocalDateTime.now());   // Perfectly matches your 'timestamp' variable
        post.setIsAnonymous(dto.getIsAnonymous());// From your new DTO
        
        // Setting your default values just to be safe
        post.setUpvoteCount(0);
        post.setIsResolved(false);

        return forumPostRepository.save(post);
    }

    // --- 2. METHOD TO GET ALL POSTS ---
    public List<ForumPostResponseDto> getAllPosts() {
        // Uses your newly updated repository method!
        List<ForumPost> posts = forumPostRepository.findAllByOrderByTimestampDesc();

        return posts.stream().map(post -> {
            ForumPostResponseDto dto = new ForumPostResponseDto();
            dto.setPostId(post.getPostId());
            dto.setTextContent(post.getTextContent());
            dto.setTimestamp(post.getTimestamp()); 
            
            // Checks if anonymous before revealing the name!
            if (post.getIsAnonymous() != null && post.getIsAnonymous()) {
                dto.setAuthorName("Anonymous Student");
            } else {
                dto.setAuthorName(post.getAuthor().getStudentProfile().getFirstName() + " " + post.getAuthor().getStudentProfile().getLastName());
            }
            
            return dto;
        }).collect(Collectors.toList());  
    }
    // To upvote a post
    public void upvotePost(Long postId) {
        ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        // Add 1 to the current score
        post.setUpvoteCount(post.getUpvoteCount() + 1);
        forumPostRepository.save(post);
    }
    // to comment
    
    public void addComment(Long postId, CommentRequestDto dto) {
        ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
                
        UserAccount commentAuthor = userAccountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setForumPost(post);
        comment.setAuthor(commentAuthor);
        comment.setTextContent(dto.getTextContent());
        comment.setTimestamp(LocalDateTime.now());

        commentRepository.save(comment);
    }
    // get all comments in a post
    public List<CommentResponseDto> getCommentsForPost(Long postId) {
        ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
                
        List<Comment> comments = commentRepository.findAllByForumPostOrderByTimestampAsc(post);

        // Map the raw entities to our safe DTOs to prevent the infinite loop!
        return comments.stream().map(comment -> {
            CommentResponseDto dto = new CommentResponseDto();
            dto.setCommentId(comment.getCommentId());
            dto.setTextContent(comment.getTextContent());
            dto.setTimestamp(comment.getTimestamp());
            
            // Safely extract just the name string, leaving the raw entities behind
            dto.setAuthorName(comment.getAuthor().getStudentProfile().getFirstName() + " " + comment.getAuthor().getStudentProfile().getLastName());
            
            return dto;
        }).collect(Collectors.toList());
    }
    // to mark post as resolved
    public void markPostAsResolved(Long postId, Long accountId) {
        // find the post
        ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // check if it is the author
        if (!post.getAuthor().getAccountId().equals(accountId)) {
            throw new RuntimeException("Unauthorized: You can only resolve your own posts.");
        }
        
        post.setIsResolved(true);
        forumPostRepository.save(post);
    }
}