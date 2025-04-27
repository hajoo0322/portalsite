package com.portalSite.chatbot.dto;

import com.portalSite.common.valid.NotBlankOrWhitespace;

public record UpdateFaqRequest(
        @NotBlankOrWhitespace String question,
        @NotBlankOrWhitespace String answer
) {
}
