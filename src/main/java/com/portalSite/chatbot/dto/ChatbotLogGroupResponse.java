package com.portalSite.chatbot.dto;

import com.portalSite.chatbot.entity.ChatbotLog;
import com.portalSite.chatbot.entity.ChatbotRoom;

import java.util.List;

public record ChatbotLogGroupResponse(
        Long roomId,
        Boolean isClosed,
        List<ChatbotLogResponse> logs
) {
    public static ChatbotLogGroupResponse from(ChatbotRoom room, List<ChatbotLog> logs) {
        return new ChatbotLogGroupResponse(
                room.getId(),
                room.getIsClosed(),
                logs.stream().map(ChatbotLogResponse::from).toList()
        );
    }
}
