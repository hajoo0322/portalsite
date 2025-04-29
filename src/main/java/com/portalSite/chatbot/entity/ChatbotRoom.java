package com.portalSite.chatbot.entity;

import com.portalSite.common.BaseEntity;
import com.portalSite.common.exception.custom.CustomException;
import com.portalSite.common.exception.custom.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@Table(name = "chatbot_room")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatbotRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관관계 매핑 사용하지 않음
    private Long memberId;

    private Boolean isClosed = false;

    public static ChatbotRoom of(Long memberId) {
        return new ChatbotRoom(memberId);
    }

    public void isEqualMember(Long authUserId) {
        if (!Objects.equals(this.memberId, authUserId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    public void isClosed() {
        if (this.isClosed) {
            throw new CustomException(ErrorCode.CHATROOM_IS_ALREADY_CLOSED);
        }
    }

    private ChatbotRoom(Long memberId) {
        this.memberId = memberId;
    }
}
