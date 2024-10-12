package com.ramonlence.social_biznaga.messages;

import com.ramonlence.social_biznaga.messages.dto.MessageDTO;
import com.ramonlence.social_biznaga.messages.dto.MessageResponseDTO;
import com.ramonlence.social_biznaga.messages.model.Message;
import com.ramonlence.social_biznaga.messages.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@AllArgsConstructor
public class MessageController {

    private MessageService messageService;

    @PostMapping
    public ResponseEntity<Void> createMessage(@Validated @RequestBody MessageDTO messageDto, Authentication authentication) {
        String username = authentication.getName();
        messageService.createMessage(username, messageDto);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<MessageResponseDTO>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        List<MessageResponseDTO> response = mapMessagesToMessageResponseDTO(messages);
        return ResponseEntity.ok(response);
    }

    private List<MessageResponseDTO> mapMessagesToMessageResponseDTO(List<Message> messages) {
        return messages.stream()
                .map(message -> MessageResponseDTO.builder()
                        .content(message.getContent())
                        .createdAt(message.getCreatedAt())
                        .userName(message.getUser().getUsername())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable String username) {
        List<Message> userMessages = messageService.getMessagesByUser(username);
        return ResponseEntity.ok(userMessages);
    }
}
