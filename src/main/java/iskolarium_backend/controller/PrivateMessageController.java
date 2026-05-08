package iskolarium_backend.controller;

import iskolarium_backend.dto.MessageRequestDto;
import iskolarium_backend.dto.MessageResponseDto;
import iskolarium_backend.service.PrivateMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class PrivateMessageController {

    @Autowired
    private PrivateMessageService messageService;

    // POST: http://localhost:8080/api/messages/send
    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessageRequestDto dto) {
        try {
            messageService.sendMessage(dto);
            return ResponseEntity.ok("Message sent!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // GET: http://localhost:8080/api/messages/conversation?viewerId=1&otherUserId=2
    @GetMapping("/conversation")
    public ResponseEntity<?> getConversation(@RequestParam Long viewerId, @RequestParam Long otherUserId) {
        try {
            List<MessageResponseDto> thread = messageService.getConversation(viewerId, otherUserId);
            return ResponseEntity.ok(thread);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    // GET: http://localhost:8080/api/messages/conversations?userId=1
    @GetMapping("/conversations")
    public ResponseEntity<?> getConversations(@RequestParam Long userId) {
        try {
            List<Map<String, Object>> conversations = messageService.getConversationsList(userId);
            return ResponseEntity.ok(conversations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    // GET: http://localhost:8080/api/messages/between?user1Id=1&user2Id=2
    @GetMapping("/between")
    public ResponseEntity<?> getConversationBetween(
            @RequestParam Long user1Id, 
            @RequestParam Long user2Id) {
            try {
            List<MessageResponseDto> messages = messageService.getConversation(user1Id, user2Id);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}