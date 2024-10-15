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
        Long userId = Long.parseLong(authentication.getCredentials().toString());
        messageService.createMessage(username, userId, messageDto);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<List<MessageResponseDTO>> getAllMessages(@PathVariable int page) {
        List<MessageResponseDTO> messages = messageService.getAllMessagesByPage(page);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesByUser(@PathVariable String username) {
        List<MessageResponseDTO> userMessages = messageService.getMessagesByUser(username);
        return ResponseEntity.ok(userMessages);
    }
}
