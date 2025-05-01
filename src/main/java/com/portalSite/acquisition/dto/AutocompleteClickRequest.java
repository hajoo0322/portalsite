package com.portalSite.acquisition.dto;

import java.time.LocalDateTime;

public record AutocompleteClickRequest(
        String keyword,
        String suggestion,
        Integer position,
        LocalDateTime timeStamp
) {
}
