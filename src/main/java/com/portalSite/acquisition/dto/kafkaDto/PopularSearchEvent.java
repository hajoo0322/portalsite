package com.portalSite.acquisition.dto.kafkaDto;

import com.portalSite.acquisition.EventType;

import java.time.LocalDateTime;

public record PopularSearchEvent(
        EventType eventType,
        String keyword,
        Boolean resultClicked,
        String clickedDocumentId,
        LocalDateTime timeStamp,
        Integer dwellTime,
        LocalDateTime exitedAt

) {
}
