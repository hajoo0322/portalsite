package com.portalSite.chatbot;

import com.portalSite.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chatbot_faq")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatbotFaq extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String answer;

    // 연관관계 매핑 사용하지 않음
    private Long adminId;
    
    public static ChatbotFaq of(String question, String answer, Long adminId) {
        return new ChatbotFaq(question, answer, adminId);
    }

    private ChatbotFaq(String question, String answer, Long adminId) {
        this.question = question;
        this.answer = answer;
        this.adminId = adminId;
    }

    public void update(Long memberId, UpdateFaqRequest request) {
        if (request == null) {
            return;
        }
        this.adminId = memberId;

        if (request.answer() != null && !request.answer().trim().isEmpty()) {
            this.answer = request.answer();
        }
        if (request.question() != null && !request.question().trim().isEmpty()) {
            this.question = request.question();
        }
    }
}
