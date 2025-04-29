package com.portalSite.common.infra;

import com.portalSite.chatbot.dto.ChatbotAnswerMessage;
import com.portalSite.chatbot.repository.MessageBroadcaster;
import com.portalSite.chatbot.repository.MessageBroker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SimpleMessageBroker implements MessageBroker {

    private final MessageBroadcaster broadcaster;

    @Override
    public void publish(ChatbotAnswerMessage message) {
        System.out.println("브로커");
        broadcaster.send("");
    }
}
