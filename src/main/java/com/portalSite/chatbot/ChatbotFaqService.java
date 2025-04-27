package com.portalSite.chatbot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatbotFaqService {

    private final ChatbotFaqRepository chatbotFaqRepository;

    public ChatbotFaqResponse addFaq(Long memberId, AddFaqRequest request) {
        ChatbotFaq faq = ChatbotFaq.of(request.question(), request.answer(), memberId);
        chatbotFaqRepository.save(faq);
        return ChatbotFaqResponse.from(faq);
    }

    @Transactional
    public ChatbotFaqResponse updateFaq(Long faqId, Long memberId, UpdateFaqRequest request) {
        ChatbotFaq faq = chatbotFaqRepository.findById(faqId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 챗봇"));
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
