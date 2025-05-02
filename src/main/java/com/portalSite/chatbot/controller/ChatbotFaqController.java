package com.portalSite.chatbot.controller;

import com.portalSite.chatbot.dto.ChatbotLogGroupResponse;
import com.portalSite.chatbot.dto.ChatbotRoomResponse;
import com.portalSite.chatbot.dto.QuestionFaqRequest;
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
            @RequestBody QuestionFaqRequest request
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

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatbotLogGroupResponse> getRoomLogs(
            @PathVariable Long roomId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        ChatbotLogGroupResponse logs = chatbotFaqService.getRoomLogs(roomId, authUser.memberId());
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }

    @PostMapping("/feedback/{logId}")
    public ResponseEntity<Void> feedback(
            @PathVariable Long logId,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam String feedback
    ) {
        chatbotFaqService.feedback(logId, authUser.memberId(), feedback);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/exit/{roomId}")
    public ResponseEntity<Void> exit(
            @PathVariable Long roomId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        chatbotFaqService.exit(roomId, authUser.memberId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
