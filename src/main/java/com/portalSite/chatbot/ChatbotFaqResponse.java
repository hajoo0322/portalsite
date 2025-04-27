package com.portalSite.chatbot;

public record ChatbotFaqResponse(
        Long faqId,
        String question,
        String answer,
        Long memberId
) {
    public static ChatbotFaqResponse from(ChatbotFaq faq) {
        return new ChatbotFaqResponse(
                faq.getId(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getAdminId()
        );
    }
}
