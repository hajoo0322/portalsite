package com.portalSite.chatbot.repository;

import com.portalSite.chatbot.dto.ChatbotAnswerMessage;

public interface MessageBroadcaster {

    void send(ChatbotAnswerMessage message);
}
