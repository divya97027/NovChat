package com.novchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.novchat.model.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
