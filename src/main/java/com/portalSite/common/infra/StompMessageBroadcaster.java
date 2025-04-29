package com.portalSite.common.infra;

import com.portalSite.chatbot.repository.MessageBroadcaster;
import org.springframework.stereotype.Repository;

@Repository
public class StompMessageBroadcaster implements MessageBroadcaster {

    @Override
    public void send(String message) {
        System.out.println("브로드캐스터");
    }
}
