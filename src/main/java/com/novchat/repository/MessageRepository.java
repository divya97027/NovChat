package com.novchat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.novchat.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByRoomIdOrderBySentAtAsc(Long roomId);
}
