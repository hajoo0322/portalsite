package com.portalSite.acquisition.dto.request;

import com.portalSite.acquisition.EventType;

import java.time.LocalDateTime;

public record SearchClickRequest(
        EventType eventType,
        String keyword,
        Boolean resultClicked,
        String clickedDocumentId,
        LocalDateTime timeStamp
) {
}
