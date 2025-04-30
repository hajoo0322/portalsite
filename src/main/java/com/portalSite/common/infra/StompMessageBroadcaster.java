package com.portalSite.common.infra;

import com.portalSite.chatbot.dto.ChatbotAnswerMessage;
import com.portalSite.chatbot.repository.MessageBroadcaster;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StompMessageBroadcaster implements MessageBroadcaster {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void send(ChatbotAnswerMessage message) {
        messagingTemplate.convertAndSend("/topic/message", message);
    }
}
