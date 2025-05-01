package com.portalSite.chatbot.dto;

import com.portalSite.chatbot.entity.ChatbotLog;

import java.time.LocalDateTime;

public record ChatbotLogResponse(
        Long logId,
        LocalDateTime createdAt,
        String question,
        String answer,
        String feedback
) {
    public static ChatbotLogResponse from(ChatbotLog log) {
        return new ChatbotLogResponse(
                log.getId(),
                log.getCreatedAt(),
                log.getQuestion(),
                log.getAnswer(),
                log.getFeedback().toString()
        );
    }
}