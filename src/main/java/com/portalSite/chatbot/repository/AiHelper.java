package com.portalSite.chatbot.repository;

public interface AiHelper {

    // 사용자 요청에서 키워드 추출
    String extractKeywords(String userInput);

    // 정제된 답변 출력
    String refineAnswer(String answerText);
}
