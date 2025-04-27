package com.portalSite.chatbot;

import com.portalSite.common.valid.NotBlankOrWhitespace;

public record UpdateFaqRequest(
        @NotBlankOrWhitespace String question,
        @NotBlankOrWhitespace String answer
) {
}
