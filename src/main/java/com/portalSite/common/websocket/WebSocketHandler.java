package com.portalSite.common.websocket;

import com.portalSite.security.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;

@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final WebSocketSessionManager sessionManager;
    private final JwtUtil jwtUtil;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long memberId = (Long) session.getAttributes().get("memberId");
        sessionManager.addSession(memberId, session);
    }

    @Override
    public void afterConnectionClosed(
            WebSocketSession session,
            @NonNull CloseStatus status
    ) {
        String token = Objects.requireNonNull(session.getUri()).getQuery().split("=")[1];
        Claims claims = jwtUtil.extractClaims(jwtUtil.substringToken(token));
        String[] data = claims.getSubject().split(":");
        Long memberId = Long.valueOf(data[0]);

        sessionManager.removeSession(memberId, session);
    }
}