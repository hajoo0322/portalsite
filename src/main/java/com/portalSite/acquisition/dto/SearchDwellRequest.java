package com.portalSite.acquisition.dto;

import java.time.LocalDateTime;

public record SearchDwellRequest(
        String keyword,
        Integer dwellTime,
        LocalDateTime exitedAt
) {
}
