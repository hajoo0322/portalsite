package com.portalSite.security;

import com.portalSite.member.entity.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public record AuthUser(
        Long memberId,
        Collection<? extends GrantedAuthority> authorities
) {

    public static AuthUser of(Long memberId, MemberRole role) {
        return new AuthUser(
                memberId,
                List.of(new SimpleGrantedAuthority(role.getRoleName()))
        );
    }
}