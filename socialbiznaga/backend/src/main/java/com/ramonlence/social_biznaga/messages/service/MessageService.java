package com.ramonlence.social_biznaga.messages.service;

import com.ramonlence.social_biznaga.messages.dto.MessageDTO;
import com.ramonlence.social_biznaga.messages.model.Message;
import com.ramonlence.social_biznaga.messages.repository.MessageRepository;
import com.ramonlence.social_biznaga.users.model.User;
import com.ramonlence.social_biznaga.users.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {

    private MessageRepository messageRepository;

    private UserRepository userRepository;

    public Message createMessage(String username, MessageDTO messageDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Message message = Message.builder().content(messageDto.getContent()).createdAt(LocalDateTime.now()).user(user).build();
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Message> getMessagesByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return messageRepository.findByUser(user);
    }
}
