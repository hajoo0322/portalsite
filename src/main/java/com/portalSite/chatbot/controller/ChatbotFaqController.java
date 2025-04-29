package com.portalSite.chatbot.controller;

import com.portalSite.chatbot.dto.FaqQuestionRequest;
import com.portalSite.chatbot.service.ChatbotFaqService;
import com.portalSite.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/faq")
@RequiredArgsConstructor
public class ChatbotFaqController {

    private final ChatbotFaqService chatbotFaqService;

    @PostMapping("/{roomId}")
    public ResponseEntity<Void> handleFaqQuestion(
            @PathVariable Long roomId,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody FaqQuestionRequest request
    ) {
        chatbotFaqService.handleQuestion(roomId, authUser.memberId(), request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
