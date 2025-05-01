package com.portalSite.acquisition.dto;

import java.time.LocalDateTime;

public record SearchDwellRequest(
        Long userId,
        String keyword,
        Integer dwellTime,
        LocalDateTime exitedAt
) {
}
