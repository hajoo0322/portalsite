package com.portalSite.chatbot.service;

import com.portalSite.chatbot.dto.ChatbotAnswerMessage;
import com.portalSite.chatbot.dto.FaqQuestionRequest;
import com.portalSite.chatbot.entity.ChatbotLog;
import com.portalSite.chatbot.entity.ChatbotRoom;
import com.portalSite.chatbot.repository.*;
import com.portalSite.common.exception.custom.CustomException;
import com.portalSite.common.exception.custom.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatbotFaqService {

    private final ChatbotFaqRepository chatbotFaqRepository;
    private final ChatbotLogRepository chatbotLogRepository;
    private final ChatbotRoomRepository chatbotRoomRepository;
    private final MessageBroker broker;
    private final AiHelper aiHelper;

    @Transactional
    public void handleQuestion(Long roomId, Long memberId, FaqQuestionRequest request) {
        ChatbotRoom chatbotRoom = chatbotRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
        chatbotRoom.isClosed();
        chatbotRoom.isEqualMember(memberId);
        String keyword = aiHelper.extractKeywords(request.input());
        String rawAnswer = chatbotFaqRepository.findAnswerByFullText(keyword);
        String answer = (rawAnswer != null && !rawAnswer.isBlank())
                ? aiHelper.refineAnswer(rawAnswer)
                : "아직 답변이 준비되지 않았습니다. 1대1 고객센터를 이용해주세요.";
        ChatbotLog chatbotLog = chatbotLogRepository.save(ChatbotLog.of(chatbotRoom, request.input(), answer));
        broker.publish(ChatbotAnswerMessage.from(chatbotLog.getId(), answer));
    }
}
