package com.portalSite.chatbot.dto;

import com.portalSite.chatbot.entity.ChatbotLog;
import com.portalSite.chatbot.entity.ChatbotRoom;

import java.time.LocalDateTime;

public record ChatbotRoomResponse(
        Long id,
        LocalDateTime lastSentAt,
        String lastSentContent,
        Boolean isClosed
) {
    public ChatbotRoomResponse(ChatbotRoom room, ChatbotLog latestLog) {
        this(
                room.getId(),
                latestLog != null ? latestLog.getCreatedAt() : null,
                latestLog != null ? latestLog.getAnswer() : null,
                room.getIsClosed()
        );
    }
}
