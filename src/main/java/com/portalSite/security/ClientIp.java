package com.portalSite.security;

import jakarta.servlet.http.HttpServletRequest;

import java.io.Serializable;

public record ClientIp(String clientIp) implements Serializable {

    public ClientIp(HttpServletRequest request) {
        this(extractClientIp(request));
    }

    private static String extractClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return ip !=null ? ip.split(",")[0] : request.getRemoteAddr();
    }
}
