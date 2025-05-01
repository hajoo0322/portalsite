package com.portalSite.acquisition.dto.request;

import java.time.LocalDateTime;

public record SearchDwellRequest(
        String keyword,
        Integer dwellTime,
        LocalDateTime exitedAt
) {
}
