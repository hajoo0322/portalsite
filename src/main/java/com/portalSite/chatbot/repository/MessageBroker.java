package com.portalSite.chatbot.repository;

import com.portalSite.chatbot.dto.ChatbotAnswerMessage;

public interface MessageBroker {
    void publish(ChatbotAnswerMessage message);
}
