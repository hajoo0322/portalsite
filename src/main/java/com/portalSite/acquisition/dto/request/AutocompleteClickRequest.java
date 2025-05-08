package com.portalSite.acquisition.dto.request;

import com.portalSite.acquisition.EventType;

import java.time.LocalDateTime;

public record AutocompleteClickRequest(
        EventType eventType,
        String keyword,
        String suggestion,
        Integer position,
        LocalDateTime timeStamp
) {
}
