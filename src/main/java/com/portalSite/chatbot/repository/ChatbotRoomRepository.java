package com.portalSite.chatbot.repository;

import com.portalSite.chatbot.entity.ChatbotRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatbotRoomRepository extends JpaRepository<ChatbotRoom, Long> {
}
