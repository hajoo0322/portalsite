package com.portalSite.acquisition.dto;

import java.time.LocalDateTime;

public record SearchClickRequest(
        String keyword,
        Boolean resultClicked,
        String clickedDocumentId,
        LocalDateTime timeStamp
) {
}
