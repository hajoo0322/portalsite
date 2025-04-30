package com.portalSite.mock;

import com.portalSite.chatbot.entity.ChatbotFaq;

public class MockFaqFactory {

    public static ChatbotFaq createFaq(Long id, String question, String answer, Long adminId) {
        ChatbotFaq faq = ChatbotFaq.of(question, answer, adminId);
        setId(faq, id);
        return faq;
    }

    private static void setId(ChatbotFaq faq, Long id) {
        try {
            var field = ChatbotFaq.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(faq, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
