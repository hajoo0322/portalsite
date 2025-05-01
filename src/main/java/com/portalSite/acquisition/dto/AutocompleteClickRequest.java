package com.portalSite.acquisition.dto;

import java.time.LocalDateTime;

public record AutocompleteClickRequest(
        Long userId,
        String keyword,
        String suggestion,
        Integer position,
        LocalDateTime timeStamp
) {
}
