package com.portalSite.chatbot.entity;

import com.portalSite.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chatbot_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatbotLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatbot_room_id")
    private ChatbotRoom chatbotRoom;

    private String question;

    private String answer;

    public static ChatbotLog of(ChatbotRoom room, String question, String answer) {
        return new ChatbotLog(room, question, answer);
    }

    private ChatbotLog(ChatbotRoom room, String question, String answer) {
        this.chatbotRoom = room;
        this.question = question;
        this.answer = answer;
    }
}
