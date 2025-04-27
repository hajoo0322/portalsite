package com.portalSite.chatbot.controller;

import com.portalSite.chatbot.service.ChatbotFaqService;
import com.portalSite.chatbot.dto.AddFaqRequest;
import com.portalSite.chatbot.dto.ChatbotFaqResponse;
import com.portalSite.chatbot.dto.UpdateFaqRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faq")
@RequiredArgsConstructor
public class ChatbotFaqController {

    private final ChatbotFaqService chatbotFaqService;

    @PostMapping
    public ResponseEntity<ChatbotFaqResponse> addChatbotFaq(
            //TODO ADMIN authority + memberId
            @Valid @RequestBody AddFaqRequest request
    ) {
        Long memberId = 1L;
        ChatbotFaqResponse response = chatbotFaqService.addFaq(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{faqId}")
    public ResponseEntity<ChatbotFaqResponse> updateChatbotFaq(
            //TODO ADMIN authority + memberId
            @PathVariable Long faqId,
            @Valid @RequestBody UpdateFaqRequest request
    ) {
        Long memberId = 1L;
        ChatbotFaqResponse response = chatbotFaqService.updateFaq(faqId, memberId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //TODO ADMIN authority
    @DeleteMapping("/{faqId}")
    public ResponseEntity<Void> deleteChatbotFaq(
            @PathVariable Long faqId
    ) {
        chatbotFaqService.deleteFaq(faqId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //TODO ADMIN authority
    @GetMapping
    public ResponseEntity<List<ChatbotFaqResponse>> getAllChatbotFaq() {
        List<ChatbotFaqResponse> response = chatbotFaqService.getAllFaq();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
