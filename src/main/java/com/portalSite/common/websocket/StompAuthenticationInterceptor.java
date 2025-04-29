package com.portalSite.common.websocket;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * STOMP 사용자 인증 처리 + 세션 추가
 */
@Component
@RequiredArgsConstructor
public class StompAuthenticationInterceptor implements ChannelInterceptor {

    private final WebSocketSessionManager sessionManager;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Long memberId = (Long) Objects.requireNonNull(accessor.getSessionAttributes()).get("memberId");
            accessor.setUser(() -> String.valueOf(memberId));
            sessionManager.addSession(memberId, accessor.getSessionId());

        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            sessionManager.removeSession(accessor.getSessionId());
        }
        return message;
    }
}