package com.portalSite.mock;

import com.portalSite.chatbot.entity.ChatbotLog;
import com.portalSite.chatbot.entity.ChatbotRoom;

public class MockChatbotLogFactory {
    public static ChatbotLog createLog(Long id, ChatbotRoom room, String q, String a) {
        ChatbotLog log = ChatbotLog.of(room, q, a);
        setId(log, id);
        return log;
    }

    private static void setId(ChatbotLog log, Long id) {
        try {
            var field = ChatbotLog.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(log, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
