package com.portalSite.chatbot.repository;

import com.portalSite.chatbot.entity.ChatbotFaq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatbotFaqRepository extends JpaRepository<ChatbotFaq, Long> {

    // n-gram 풀텍스트 인덱스 사용
    @Query(
            value = "SELECT answer " +
                    "FROM chatbot_faq " +
                    "WHERE MATCH(question) AGAINST (?1 IN BOOLEAN MODE) " +
                    "LIMIT 1",
            nativeQuery = true
    )
    String findAnswerByFullText(String keywordExpression);
}
