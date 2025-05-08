package com.portalSite.acquisition.dto.request;

import com.portalSite.acquisition.EventType;

import java.time.LocalDateTime;

public record SearchDwellRequest(
        EventType eventType,
        String keyword,
        Integer dwellTime,
        LocalDateTime exitedAt
) {
}
