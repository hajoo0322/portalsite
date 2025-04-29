package com.portalSite.chatbot.service;

import com.portalSite.chatbot.entity.ChatbotFaq;
import com.portalSite.chatbot.repository.ChatbotFaqRepository;
import com.portalSite.chatbot.dto.AddFaqRequest;
import com.portalSite.chatbot.dto.ChatbotFaqResponse;
import com.portalSite.chatbot.dto.UpdateFaqRequest;
import com.portalSite.common.exception.custom.CustomException;
import com.portalSite.common.exception.custom.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatbotAdminService {

    private final ChatbotFaqRepository chatbotFaqRepository;

    public ChatbotFaqResponse addFaq(Long memberId, AddFaqRequest request) {
        ChatbotFaq faq = ChatbotFaq.of(request.question(), request.answer(), memberId);
        chatbotFaqRepository.save(faq);
        return ChatbotFaqResponse.from(faq);
    }

    @Transactional
    public ChatbotFaqResponse updateFaq(Long faqId, Long memberId, UpdateFaqRequest request) {
        ChatbotFaq faq = chatbotFaqRepository.findById(faqId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_FAQ_NOT_FOUND));
        faq.update(memberId, request);
        chatbotFaqRepository.save(faq);
        return ChatbotFaqResponse.from(faq);
    }

    public void deleteFaq(Long faqId) {
        chatbotFaqRepository.deleteById(faqId);
    }

    public List<ChatbotFaqResponse> getAllFaq() {
        return chatbotFaqRepository.findAll()
                .stream()
                .map(ChatbotFaqResponse::from)
                .toList();
    }
}
