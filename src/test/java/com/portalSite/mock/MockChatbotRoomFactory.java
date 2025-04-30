package com.portalSite.mock;

import com.portalSite.chatbot.entity.ChatbotRoom;
import com.portalSite.util.SetUtil;

public class MockChatbotRoomFactory {
    public static ChatbotRoom createRoom(Long id, Long memberId, boolean isClosed) {
        ChatbotRoom room = ChatbotRoom.of(memberId);
        if (isClosed) {
            try {
                var field = ChatbotRoom.class.getDeclaredField("isClosed");
                field.setAccessible(true);
                field.set(room, true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        SetUtil.setId(room, id);
        return room;
    }
}
