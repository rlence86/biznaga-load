package com.ramonlence.social_biznaga.messages.service;

import com.ramonlence.social_biznaga.messages.dto.MessageDTO;
import com.ramonlence.social_biznaga.messages.dto.MessageResponseDTO;
import com.ramonlence.social_biznaga.messages.model.Message;
import com.ramonlence.social_biznaga.messages.repository.MessageRepository;
import com.ramonlence.social_biznaga.users.model.User;
import com.ramonlence.social_biznaga.users.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageService {

    private static int PAGE_SIZE = 20;

    private MessageRepository messageRepository;

    private UserRepository userRepository;

    public Message createMessage(String userName, Long userId, MessageDTO messageDto) {

        Message message = Message.builder()
                .content(messageDto.getContent())
                .createdAt(LocalDateTime.now())
                .userId(userId)
                .userName(userName)
                .build();
        return messageRepository.save(message);
    }

    @Cacheable(value = "messagesCache")
    public List<MessageResponseDTO> getAllMessagesByPage(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return mapMessagesToMessageResponseDTO(messageRepository.findAllByOrderByCreatedAtDesc(pageable));
    }

    public List<MessageResponseDTO> getMessagesByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return mapMessagesToMessageResponseDTO(messageRepository.findByUser(user));
    }


    private List<MessageResponseDTO> mapMessagesToMessageResponseDTO(List<Message> messages) {
        return messages.stream()
                .map(message -> MessageResponseDTO.builder()
                        .content(message.getContent())
                        .createdAt(message.getCreatedAt())
                        .userName(message.getUserName())
                        .build()
                )
                .collect(Collectors.toList());
    }

}
