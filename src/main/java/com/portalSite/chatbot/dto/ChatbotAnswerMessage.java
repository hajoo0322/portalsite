package com.portalSite.chatbot.dto;

public record ChatbotAnswerMessage(
        Long logId,
        String answer
) {
    public static ChatbotAnswerMessage from(Long logId, String answer) {
        return new ChatbotAnswerMessage(
                logId, answer
        );
    }
}
