package com.portalSite.chatbot;

import com.portalSite.chatbot.dto.AddFaqRequest;
import com.portalSite.chatbot.dto.ChatbotFaqResponse;
import com.portalSite.chatbot.dto.UpdateFaqRequest;
import com.portalSite.chatbot.entity.ChatbotFaq;
import com.portalSite.chatbot.repository.ChatbotFaqRepository;
import com.portalSite.chatbot.service.ChatbotAdminService;
import com.portalSite.common.exception.custom.CustomException;
import com.portalSite.common.exception.custom.ErrorCode;
import com.portalSite.mock.MockFaqFactory;
import com.portalSite.mock.MockMemberFactory;
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
class ChatbotAdminServiceTest {

    @Mock private ChatbotFaqRepository faqRepository;
    @InjectMocks private ChatbotAdminService adminService;

    private final Long adminId = MockMemberFactory.createAdmin(1L).getId();
    private ChatbotFaq dummyFaq;

    @BeforeEach
    void setup() {
        dummyFaq = MockFaqFactory.createFaq(99L, "질문?", "답변입니다.", adminId);
    }

    @Test
    void addFaq_정상_생성() {
        AddFaqRequest request = new AddFaqRequest("무엇인가요?", "이렇습니다.");
        when(faqRepository.save(any(ChatbotFaq.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChatbotFaqResponse response = adminService.addFaq(adminId, request);

        assertThat(response.question()).isEqualTo("무엇인가요?");
        assertThat(response.answer()).isEqualTo("이렇습니다.");
        assertThat(response.memberId()).isEqualTo(adminId);
        verify(faqRepository).save(any(ChatbotFaq.class));
    }

    @Test
    void updateFaq_정상_수정() {
        UpdateFaqRequest updateRequest = new UpdateFaqRequest("업데이트된 질문", "업데이트된 답변");
        when(faqRepository.findById(99L)).thenReturn(Optional.of(dummyFaq));

        ChatbotFaqResponse response = adminService.updateFaq(99L, adminId, updateRequest);

        assertThat(response.question()).isEqualTo("업데이트된 질문");
        assertThat(response.answer()).isEqualTo("업데이트된 답변");
        verify(faqRepository).findById(99L);
    }

    @Test
    void updateFaq_존재하지_않으면_예외() {
        when(faqRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminService.updateFaq(123L, adminId, new UpdateFaqRequest("q", "a")))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.CHAT_FAQ_NOT_FOUND.getMessage());
    }

    @Test
    void updateFaq_빈값_무시하고_업데이트() {
        UpdateFaqRequest partial = new UpdateFaqRequest(" ", "   ");
        when(faqRepository.findById(99L)).thenReturn(Optional.of(dummyFaq));

        ChatbotFaqResponse response = adminService.updateFaq(99L, adminId, partial);

        assertThat(response.question()).isEqualTo("질문?");
        assertThat(response.answer()).isEqualTo("답변입니다.");
    }

    @Test
    void deleteFaq_정상_삭제() {
        doNothing().when(faqRepository).deleteById(99L);

        adminService.deleteFaq(99L);

        verify(faqRepository).deleteById(99L);
    }

    @Test
    void getAllFaq_정상_조회() {
        when(faqRepository.findAll()).thenReturn(List.of(dummyFaq));

        List<ChatbotFaqResponse> result = adminService.getAllFaq();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).question()).isEqualTo("질문?");
    }
}
