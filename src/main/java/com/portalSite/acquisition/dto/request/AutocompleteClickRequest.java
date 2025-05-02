package com.portalSite.acquisition.dto.request;

import java.time.LocalDateTime;

public record AutocompleteClickRequest(
        String keyword,
        String suggestion,
        Integer position,
        LocalDateTime timeStamp
) {
}
