package com.portalSite.chatbot.service;

import com.portalSite.chatbot.dto.*;
import com.portalSite.chatbot.entity.ChatbotLog;
import com.portalSite.chatbot.entity.ChatbotRoom;
import com.portalSite.chatbot.entity.Feedback;
import com.portalSite.chatbot.repository.*;
import com.portalSite.common.exception.custom.CustomException;
import com.portalSite.common.exception.custom.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatbotFaqService {

    private final ChatbotFaqRepository chatbotFaqRepository;
    private final ChatbotLogRepository chatbotLogRepository;
    private final ChatbotRoomRepository chatbotRoomRepository;
    private final MessageBroker broker;
    private final AiHelper aiHelper;

    public Long createRoom(Long memberId) {
        ChatbotRoom chatbotRoom = ChatbotRoom.of(memberId);
        chatbotRoomRepository.save(chatbotRoom);
        return chatbotRoom.getId();
    }

    /**
     * Flow 사용자 질문에 대한 AI 응답 처리
     * > 사용자 입력값에서 핵심 키워드(명사) 추출 (LLM 호출)
     * > 키워드를 기반으로 FAQ 에서 관련 answer 조회 (Full-Text Search)
     * > 조회된 answer 를 자연스럽게 정제 (LLM 호출)
     * > 결과를 DB에 저장 (질문 + 응답)
     * > 응답을 WebSocket 으로 전송
     */
    @Transactional
    public void handleQuestion(Long roomId, Long memberId, QuestionFaqRequest request) {
        ChatbotRoom chatbotRoom = getChatbotRoom(roomId);
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

    public List<ChatbotRoomResponse> getMyRooms(Long memberId) {
        return chatbotRoomRepository.findAllWithLatestLog(memberId);
    }

    public ChatbotLogGroupResponse getRoomLogs(Long roomId, Long memberId) {
        ChatbotRoom chatbotRoom = getChatbotRoom(roomId);
        chatbotRoom.isEqualMember(memberId);
        List<ChatbotLog> chatbotLogs = chatbotLogRepository.findAllByChatbotRoomOrderByCreatedAtAsc(chatbotRoom);
        return ChatbotLogGroupResponse.from(chatbotRoom, chatbotLogs);
    }

    @Transactional
    public void feedback(Long logId, Long memberId, String feedback) {
        ChatbotLog chatbotLog = getChatbotLog(logId);
        chatbotLog.getChatbotRoom().isEqualMember(memberId);
        chatbotLog.getChatbotRoom().isClosed();
        chatbotLog.feedback(Feedback.fromString(feedback));
    }

    @Transactional
    public void exit(Long roomId, Long memberId) {
        ChatbotRoom chatbotRoom = getChatbotRoom(roomId);
        chatbotRoom.isClosed();
        chatbotRoom.isEqualMember(memberId);
        chatbotRoom.exit();
    }

    /**
     * Helper
     */
    private ChatbotRoom getChatbotRoom(Long roomId) {
        return chatbotRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
    }

    private ChatbotLog getChatbotLog(Long logId) {
        return chatbotLogRepository.findById(logId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_LOG_NOT_FOUND));
    }
}
