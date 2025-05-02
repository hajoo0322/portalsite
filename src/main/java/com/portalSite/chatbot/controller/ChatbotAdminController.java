package com.portalSite.chatbot.controller;

import com.portalSite.chatbot.service.ChatbotAdminService;
import com.portalSite.chatbot.dto.AddFaqRequest;
import com.portalSite.chatbot.dto.ChatbotFaqResponse;
import com.portalSite.chatbot.dto.UpdateFaqRequest;
import com.portalSite.member.entity.MemberRole;
import com.portalSite.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/faq")
@RequiredArgsConstructor
@Secured(MemberRole.Authority.ADMIN)
public class ChatbotAdminController {

    private final ChatbotAdminService chatbotAdminService;

    @PostMapping
    public ResponseEntity<ChatbotFaqResponse> addChatbotFaq(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody AddFaqRequest request
    ) {
        ChatbotFaqResponse response = chatbotAdminService.addFaq(authUser.memberId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{faqId}")
    public ResponseEntity<ChatbotFaqResponse> updateChatbotFaq(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long faqId,
            @Valid @RequestBody UpdateFaqRequest request
    ) {
        ChatbotFaqResponse response = chatbotAdminService.updateFaq(faqId, authUser.memberId(), request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{faqId}")
    public ResponseEntity<Void> deleteChatbotFaq(
            @PathVariable Long faqId
    ) {
        chatbotAdminService.deleteFaq(faqId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<ChatbotFaqResponse>> getAllChatbotFaq() {
        List<ChatbotFaqResponse> response = chatbotAdminService.getAllFaq();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
