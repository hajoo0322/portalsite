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
        //AI 를 이용해 유저 키워드 정제
        String keyword = aiHelper.extractKeywords(request.input());
        System.out.println("AI가 정제한 키워드 : " + keyword);
        //정제된 낱말 키워드를 바탕으로 ChatbotFaq 테이블의 question 컬럼(full-text-index 사용)에서 answer 컬럼 반환
        String rawAnswer = chatbotFaqRepository.findAnswerByFullText(keyword);
        System.out.println(rawAnswer);
        //반환된 answer 컬럼의 답변을, AI 로 정제시켜 최종 반환
        String answer = (rawAnswer != null && !rawAnswer.isBlank())
                ? aiHelper.refineAnswer(rawAnswer)
                : "아직 답변이 준비되지 않았습니다. 1대1 고객센터를 이용해주세요";
        System.out.println("AI가 정제한 앤서 : " + answer);
        //유저 request 와 정제된 answer 을 DB 에 저장
        ChatbotLog chatbotLog = chatbotLogRepository.save(ChatbotLog.of(chatbotRoom, request.input(), answer));
        //정제된 answer 을 브로드캐스트(채팅)
        broker.publish(ChatbotAnswerMessage.from(chatbotLog.getId(), answer));
    }
}
