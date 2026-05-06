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
import java.util.List;
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
        message.setIsAnonymous(dto.getIsAnonymous());
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

            // anonymoty check
            if (msg.getIsAnonymous() && !isSender) {
                // if anonymous and you didn't send it, mask the name
                dto.setSenderName("Anonymous Student");
            } else {
                // show their real name 
                dto.setSenderName(msg.getSender().getEmail()); 
            }

            return dto;
        }).collect(Collectors.toList());
    }
}