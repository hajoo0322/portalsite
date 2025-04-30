package com.portalSite.chatbot.repository;

import com.portalSite.chatbot.entity.ChatbotLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatbotLogRepository extends JpaRepository<ChatbotLog, Long> {
}
