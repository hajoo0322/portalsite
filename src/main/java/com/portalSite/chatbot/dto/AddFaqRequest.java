package com.portalSite.chatbot.dto;

import jakarta.validation.constraints.NotBlank;

public record AddFaqRequest(
        @NotBlank String question,
        @NotBlank String answer
) {
}
