package com.portalSite.chatbot.controller;

import com.portalSite.chatbot.dto.ChatbotRoomResponse;
import com.portalSite.chatbot.dto.FaqQuestionRequest;
import com.portalSite.chatbot.service.ChatbotFaqService;
import com.portalSite.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faq")
@RequiredArgsConstructor
public class ChatbotFaqController {

    private final ChatbotFaqService chatbotFaqService;

    // 챗봇 사용 위한 room 생성 api
    @PostMapping
    public ResponseEntity<Long> createRoom(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Long roomId = chatbotFaqService.createRoom(authUser.memberId());
        return ResponseEntity.status(HttpStatus.CREATED).body(roomId);
    }

    // 챗봇 메시지 전송 api
    @PostMapping("/{roomId}")
    public ResponseEntity<Void> handleFaqQuestion(
            @PathVariable Long roomId,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody FaqQuestionRequest request
    ) {
        chatbotFaqService.handleQuestion(roomId, authUser.memberId(), request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<List<ChatbotRoomResponse>> getMyRooms(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        List<ChatbotRoomResponse> rooms = chatbotFaqService.getMyRooms(authUser.memberId());
        return ResponseEntity.status(HttpStatus.OK).body(rooms);
    }
}
