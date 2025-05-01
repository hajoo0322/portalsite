package com.portalSite.chatbot.repository;

import com.portalSite.chatbot.dto.ChatbotRoomResponse;
import com.portalSite.chatbot.entity.ChatbotRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatbotRoomRepository extends JpaRepository<ChatbotRoom, Long> {

    @Query("""
    SELECT new com.portalSite.chatbot.dto.ChatbotRoomResponse(
        r.id,
        (
            SELECT l.createdAt FROM ChatbotLog l
            WHERE l.chatbotRoom.id = r.id
            ORDER BY l.createdAt DESC
            LIMIT 1
        ),
        (
            SELECT l.answer FROM ChatbotLog l
            WHERE l.chatbotRoom.id = r.id
            ORDER BY l.createdAt DESC
            LIMIT 1
        ),
        r.isClosed
    )
    FROM ChatbotRoom r
    WHERE r.memberId = :memberId
    """)
    List<ChatbotRoomResponse> findAllWithLatestLog(@Param("memberId") Long memberId);
}
