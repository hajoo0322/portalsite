package com.portalSite.mock;

import com.portalSite.chatbot.entity.ChatbotFaq;
import com.portalSite.util.SetUtil;

public class MockFaqFactory {

    public static ChatbotFaq createFaq(Long id, String question, String answer, Long adminId) {
        ChatbotFaq faq = ChatbotFaq.of(question, answer, adminId);
        SetUtil.setId(faq, id);
        return faq;
    }
}
