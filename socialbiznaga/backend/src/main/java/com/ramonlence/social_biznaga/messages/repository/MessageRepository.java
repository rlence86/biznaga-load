package com.ramonlence.social_biznaga.messages.repository;

import com.ramonlence.social_biznaga.messages.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByUserId(Long userId);

    List<Message> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
