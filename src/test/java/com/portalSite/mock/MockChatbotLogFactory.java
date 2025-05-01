package com.portalSite.mock;

import com.portalSite.chatbot.entity.ChatbotLog;
import com.portalSite.chatbot.entity.ChatbotRoom;
import com.portalSite.util.SetUtil;

public class MockChatbotLogFactory {

    public static ChatbotLog createLog(Long id, ChatbotRoom room, String q, String a) {
        ChatbotLog log = ChatbotLog.of(room, q, a);
        SetUtil.setId(log, id);
        return log;
    }
}
