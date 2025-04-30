package com.portalSite.mock;

import com.portalSite.auth.RegisterRequest;
import com.portalSite.member.entity.Member;
import com.portalSite.member.entity.MemberRole;

public class MockMemberFactory {

    public static Member createUser(Long id) {
        RegisterRequest request = new RegisterRequest(
                "user@example.com",
                "userLogin01",
                "userpassword12",
                "유저",
                "01012345678",
                "선량한시민"
        );
        Member user = Member.of(request, "encodedUserPassword!@#");
        setId(user, id);
        return user;
    }

    public static Member createAdmin(Long id) {
        RegisterRequest request = new RegisterRequest(
                "admin@example.com",
                "adminLogin01",
                "adminpassword34",
                "관리자",
                "01087654321",
                "자리관"
        );
        Member admin = Member.of(request, "encodedAdminPassword@!$");
        admin.changeMemberRole(MemberRole.ADMIN);
        setId(admin, id);
        return admin;
    }

    public static Member createReporter(Long id) {
        RegisterRequest request = new RegisterRequest(
                "reporter@example.com",
                "reportLogin01",
                "reporterPass56",
                "리포터",
                "01055556666",
                "박대기기자"
        );
        Member reporter = Member.of(request, "encodedReporterPwd#123");
        reporter.changeMemberRole(MemberRole.REPORTER);
        setId(reporter, id);
        return reporter;
    }

    private static void setId(Member member, Long id) {
        try {
            var field = Member.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(member, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
