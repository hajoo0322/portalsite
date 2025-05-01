package com.portalSite.acquisition.dto;

import java.time.LocalDateTime;

public record SearchClickRequest(
        Long userId,
        String keyword,
        Boolean resultClicked,
        String clickedDocumentId,
        LocalDateTime timeStamp
) {
}
