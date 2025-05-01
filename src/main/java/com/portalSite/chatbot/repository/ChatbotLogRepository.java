package com.portalSite.chatbot.repository;

import com.portalSite.chatbot.entity.ChatbotLog;
import com.portalSite.chatbot.entity.ChatbotRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatbotLogRepository extends JpaRepository<ChatbotLog, Long> {

    List<ChatbotLog> findAllByChatbotRoomOrderByCreatedAtAsc(ChatbotRoom chatbotRoom);
}
