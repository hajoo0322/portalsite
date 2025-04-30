package com.portalSite.mock;

import com.portalSite.chatbot.entity.ChatbotRoom;

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
        setId(room, id);
        return room;
    }

    private static void setId(ChatbotRoom room, Long id) {
        try {
            var field = ChatbotRoom.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(room, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
