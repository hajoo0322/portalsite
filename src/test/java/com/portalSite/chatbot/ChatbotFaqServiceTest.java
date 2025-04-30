package com.portalSite.chatbot;

import com.portalSite.chatbot.dto.QuestionFaqRequest;
import com.portalSite.chatbot.entity.ChatbotLog;
import com.portalSite.chatbot.entity.ChatbotRoom;
import com.portalSite.chatbot.entity.Feedback;
import com.portalSite.chatbot.repository.*;
import com.portalSite.chatbot.service.ChatbotFaqService;
import com.portalSite.common.exception.custom.CustomException;
import com.portalSite.common.exception.custom.ErrorCode;
import com.portalSite.mock.MockChatbotLogFactory;
import com.portalSite.mock.MockChatbotRoomFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatbotFaqServiceTest {

    @Mock private ChatbotFaqRepository faqRepository;
    @Mock private ChatbotLogRepository logRepository;
    @Mock private ChatbotRoomRepository roomRepository;
    @Mock private MessageBroker broker;
    @Mock private AiHelper aiHelper;
    @InjectMocks private ChatbotFaqService faqService;

    private Long memberId = 1L;
    private ChatbotRoom room;

    @BeforeEach
    void setup() {
        room = MockChatbotRoomFactory.createRoom(10L, memberId, false);
    }

    @Test
    void createRoom_정상작동() {
        when(roomRepository.save(any(ChatbotRoom.class))).thenAnswer(invocation -> {
            ChatbotRoom room = invocation.getArgument(0);
            setId(room, 10L);
            return room;
        });

        Long roomId = faqService.createRoom(memberId);

        assertThat(roomId).isNotNull();
        assertThat(roomId).isEqualTo(10L);
        verify(roomRepository).save(any(ChatbotRoom.class));
    }

    @Test
    void handleQuestion_정상_답변_흐름() {
        QuestionFaqRequest req = new QuestionFaqRequest("안녕하세요");
        String keyword = "안녕";
        String rawAnswer = "반갑습니다!";
        String refined = "안녕하세요! 무엇을 도와드릴까요?";
        ChatbotLog savedLog = MockChatbotLogFactory.createLog(20L, room, req.input(), refined);

        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(aiHelper.extractKeywords(anyString())).thenReturn(keyword);
        when(faqRepository.findAnswerByFullText(keyword)).thenReturn(rawAnswer);
        when(aiHelper.refineAnswer(rawAnswer)).thenReturn(refined);
        when(logRepository.save(any())).thenReturn(savedLog);

        faqService.handleQuestion(room.getId(), memberId, req);

        verify(broker).publish(any());
    }

    @Test
    void handleQuestion_FAQ_미존재시_기본답변() {
        QuestionFaqRequest req = new QuestionFaqRequest("뭐지?");
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(aiHelper.extractKeywords(anyString())).thenReturn("없음");
        when(faqRepository.findAnswerByFullText("없음")).thenReturn(null);
        when(logRepository.save(any())).thenReturn(MockChatbotLogFactory.createLog(99L, room, req.input(), "기본"));

        faqService.handleQuestion(room.getId(), memberId, req);

        verify(broker).publish(any());
    }

    @Test
    void getMyRooms_정상작동() {
        faqService.getMyRooms(memberId);
        verify(roomRepository).findAllWithLatestLog(memberId);
    }

    @Test
    void getRoomLogs_정상작동() {
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(logRepository.findAllByChatbotRoomOrderByCreatedAtAsc(room)).thenReturn(List.of());

        var result = faqService.getRoomLogs(room.getId(), memberId);

        assertThat(result.roomId()).isEqualTo(room.getId());
    }

    @Test
    void feedback_정상작동() {
        ChatbotLog log = MockChatbotLogFactory.createLog(100L, room, "hi", "hello");
        when(logRepository.findById(100L)).thenReturn(Optional.of(log));

        faqService.feedback(100L, memberId, "good");

        assertThat(log.getFeedback().name()).isEqualTo("GOOD");
    }

    @Test
    void feedback_중복시_예외() {
        ChatbotLog log = MockChatbotLogFactory.createLog(100L, room, "hi", "hello");
        log.feedback(Feedback.GOOD);
        when(logRepository.findById(100L)).thenReturn(Optional.of(log));

        assertThatThrownBy(() -> faqService.feedback(100L, memberId, "good"))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.ALREADY_FEEDBACK.getMessage());
    }

    private void setId(ChatbotRoom room, Long id) {
        try {
            var field = ChatbotRoom.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(room, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
