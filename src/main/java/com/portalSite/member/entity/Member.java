package com.portalSite.member.entity;

import com.portalSite.common.BaseEntity;
import com.portalSite.member.dto.request.MemberRequest;
import com.portalSite.member.dto.request.MemberUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(name = "login_id", nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole = MemberRole.USER;

    @Column(nullable = false)
    private boolean isDeleted = false;

    private Member(String email, String loginId, String password, String name, String phoneNumber, String nickname, MemberRole memberRole) {
        this.email = email;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.memberRole = memberRole;
    }

    public static Member of(MemberRequest request) {
        return new Member(
                request.email(),
                request.loginId(),
                request.password(),
                request.name(),
                request.phoneNumber(),
                request.nickname(),
                MemberRole.USER);
    }

    public void updateInfo(MemberUpdateRequest request) {
        this.phoneNumber = request.phoneNumber();
        this.nickname = request.nickname();
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void changeMemberRole(String memberRole) {
        if (memberRole.equalsIgnoreCase("user")) {
            this.memberRole = MemberRole.USER;
        } else if (memberRole.equalsIgnoreCase("admin")) {
            this.memberRole = MemberRole.ADMIN;
        }
        throw new RuntimeException("");
    }

    public void softDelete () {
        this.isDeleted = true;
    }

    public void restore() {
        this.isDeleted = false;
    }
}
