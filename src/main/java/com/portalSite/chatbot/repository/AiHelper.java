package com.portalSite.chatbot.repository;

public interface AiHelper {

    String extractKeywords(String userInput);

    String refineAnswer(String answerText);
}
