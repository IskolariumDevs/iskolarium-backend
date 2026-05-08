package iskolarium_backend.service;

import iskolarium_backend.dto.MessageRequestDto;
import iskolarium_backend.dto.MessageResponseDto;
import iskolarium_backend.entity.PrivateMessage;
import iskolarium_backend.entity.UserAccount;
import iskolarium_backend.repository.PrivateMessageRepository;
import iskolarium_backend.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrivateMessageService {

    @Autowired
    private PrivateMessageRepository messageRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    // send a message
    public void sendMessage(MessageRequestDto dto) {
        UserAccount sender = userAccountRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        UserAccount receiver = userAccountRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        PrivateMessage message = new PrivateMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(dto.getContent());
        
        // 🚨 FIX 1: Safely default to false since the checkbox is gone
        message.setIsAnonymous(dto.getIsAnonymous() != null ? dto.getIsAnonymous() : false);
        
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);
    }
    

    // get conversation thread
    // 'viewerId' is the person looking at their screen
    public List<MessageResponseDto> getConversation(Long viewerId, Long otherUserId) {
        List<PrivateMessage> rawMessages = messageRepository.findConversationThread(viewerId, otherUserId);

        return rawMessages.stream().map(msg -> {
            MessageResponseDto dto = new MessageResponseDto();
            dto.setMessageId(msg.getMessageId());
            dto.setContent(msg.getContent());
            dto.setTimestamp(msg.getTimestamp());
            
            // check if the viewer is the sender of this message
            boolean isSender = msg.getSender().getAccountId().equals(viewerId);
            dto.setIsYours(isSender);

            // 🚨 FIX 2: Use Boolean.TRUE.equals() to prevent NullPointerExceptions!
            if (Boolean.TRUE.equals(msg.getIsAnonymous()) && !isSender) {
                // if anonymous and you didn't send it, mask the name
                dto.setSenderName("Anonymous Student");
            } else {
                // show their real name 
                dto.setSenderName(msg.getSender().getEmail()); 
            }

            return dto;
        }).collect(Collectors.toList());
    }
    
    // get list of conversations for a user
    public List<Map<String, Object>> getConversationsList(Long userId) {
        List<PrivateMessage> allMessages = messageRepository.findAll();
        
        // Find unique conversation partners
        Set<Long> conversationPartners = new HashSet<>();
        for (PrivateMessage msg : allMessages) {
            if (msg.getSender().getAccountId().equals(userId)) {
                conversationPartners.add(msg.getReceiver().getAccountId());
            } else if (msg.getReceiver().getAccountId().equals(userId)) {
                conversationPartners.add(msg.getSender().getAccountId());
            }
        }
        
        List<Map<String, Object>> conversations = new ArrayList<>();
        for (Long partnerId : conversationPartners) {
            UserAccount partner = userAccountRepository.findById(partnerId).orElse(null);
            if (partner == null) continue;
            
            // Get last message
            List<PrivateMessage> thread = messageRepository.findConversationThread(userId, partnerId);
            if (thread.isEmpty()) continue;
            
            PrivateMessage lastMsg = thread.get(thread.size() - 1);
            
            Map<String, Object> conv = new HashMap<>();
            conv.put("otherUserId", partnerId);
            conv.put("otherUserName", partner.getStudentProfile().getFirstName() + " " + partner.getStudentProfile().getLastName());
            conv.put("lastMessage", lastMsg.getContent());
            conv.put("timestamp", lastMsg.getTimestamp());
            
            conversations.add(conv);
        }
        
        // Sort by timestamp descending
        conversations.sort((a, b) -> {
            LocalDateTime timeA = (LocalDateTime) a.get("timestamp");
            LocalDateTime timeB = (LocalDateTime) b.get("timestamp");
            return timeB.compareTo(timeA);
        });
        
        return conversations;
    }
}