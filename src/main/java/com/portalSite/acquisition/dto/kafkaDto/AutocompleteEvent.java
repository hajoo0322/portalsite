package com.portalSite.acquisition.dto.kafkaDto;

import com.portalSite.acquisition.EventType;
import com.portalSite.acquisition.dto.request.AutocompleteClickRequest;

import java.time.LocalDateTime;

public record AutocompleteEvent(
        EventType eventType,
        String keyword,
        String suggestion,
        Integer position,
        LocalDateTime timeStamp
) {
    public static AutocompleteEvent of(AutocompleteClickRequest autocompleteClickRequest, String clientIp) {
        return new AutocompleteEvent(
                autocompleteClickRequest.eventType(),
                autocompleteClickRequest.keyword(),
                autocompleteClickRequest.suggestion(),
                autocompleteClickRequest.position(),
                autocompleteClickRequest.timeStamp()
        );
    }
}
