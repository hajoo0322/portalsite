package com.portalSite.member.service;

import com.portalSite.member.dto.request.*;
import com.portalSite.member.dto.response.MemberResponse;
import com.portalSite.member.entity.Member;
import com.portalSite.member.entity.MemberRole;
import com.portalSite.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse getMember(Long memberId) {
        Member foundMember = memberRepository.findById(memberId).orElseThrow(
                () -> new RuntimeException(""));
        return MemberResponse.from(foundMember);
    }

    /**
     * 1. 대상 id, 로그인된 id, 수정 요청 바디 받음<br>
     * 2. 두 id값이 일치하는지 확인, 해당 멤버 객체 반환<br>
     * 3. 정보 수정
     */
    @Transactional
    public MemberResponse updateMember(Long memberId, Long loggedInId, MemberUpdateRequest request) {
        Member foundMember = validId(memberId, loggedInId);

        foundMember.updateInfo(request);
        return MemberResponse.from(foundMember);
    }

    /**
     * 1. 대상 id, 로그인된 id, 비밀번호 수정 요청 받음<br>
     * 2. 두 id가 일치하는지 확인, 해당 멤버 객체 반환<br>
     * 3. 멤버 객체의 비밀번호가 요청값의 구 비밀번호와 동일한지 검사<br>
     * 4. 비밀번호 수정
     */
    @Transactional
    public MemberResponse changePassword(Long memberId, Long loggedInId, MemberChangePasswordRequest request) {
        Member foundMember = validId(memberId, loggedInId);
        validPassword(foundMember, request.oldPassword());
        foundMember.updatePassword(request.newPassword());
        return MemberResponse.from(foundMember);
    }

    /**
     * 1. 대상 id, 로그인된 id 받음<br>
     * 2. 로그인된 id가 어드민인지 확인<br>
     * 3. 대상 id로 멤버 객체 조회 및 반환<br>
     * 4. 멤버 객체의 권한 수정
     */
    @Transactional
    public MemberResponse changeMemberRole(Long memberId, Long loggedInId, MemberRole role) {
        validAdmin(loggedInId);
        Member foundMember = memberRepository.findById(memberId).orElseThrow(
                () -> new RuntimeException(""));
        foundMember.changeMemberRole(role);
        return MemberResponse.from(foundMember);
    }

    /**
     * 1. 대상 id, 로그인된 id, 비밀번호 받음<br>
     * 2. 두 id가 일치하는지 확인, 해당 멤버 객체 반환<br>
     * 3. 멤버 객체의 비밀번호가 입력받은 비밀번호와 같은지 확인<br>
     * 4. 멤버객체의 isDeleted true로 변환
     */
    @Transactional
    public void softDeleteMember(Long memberId, Long loggedInId, MemberDeleteRequest request) {
        Member foundMember = validId(memberId, loggedInId);
        validPassword(foundMember, request.password());
        foundMember.softDelete();
    }

    /**
     * 1. request, 로그인된 ID 입력받음
     * 2. 로그인된 id가 어드민인지 확인<br>
     * 3. email 기준 멤버 객체 조회, request와 대조.
     * 4. 일치할 시 멤버 객체의 isDeleted false 로 변환
     */
    @Transactional
    public MemberResponse restoreMember(MemberRestoreRequest request, Long loggedInId) {
        validAdmin(loggedInId);
        Member foundMember = memberRepository.findByEmail(request.email()).orElseThrow(
                () -> new RuntimeException(""));
        if (!foundMember.getPassword().equals(request.password()) ||
                !foundMember.getName().equals(request.name()) ||
                !foundMember.getPhoneNumber().equals(request.phoneNumber())) {
            throw new RuntimeException("");
        }

        foundMember.restore();
        return MemberResponse.from(foundMember);
    }

    /**
     * 1. 대상 id, 현재 로그인된 id 각각 null인지 검사<br>
     * 2. 대상 id와 로그인한 id가 같은지 검사<br>
     * 3. 위 조건에 걸러지지 않을 경우 대상 id 로 맴버 객체 조회 및 반환
     */
    private Member validId(Long memberId, Long loggedInId) {
        if (memberId == null || loggedInId == null) {
            throw new RuntimeException("");
        }

        if (!memberId.equals(loggedInId)) {
            throw new RuntimeException("");
        }

        return memberRepository.findById(memberId).orElseThrow(
                () -> new RuntimeException(""));
    }

    private void validPassword(Member member, String currentPassword) {
        if (!member.getPassword().equals(currentPassword)) {
            throw new RuntimeException("");
        }
    }

    private void validAdmin(Long loggedInId) {
        Member foundMember = memberRepository.findById(loggedInId).orElseThrow(
                () -> new RuntimeException(""));
        if (foundMember.getMemberRole() != MemberRole.ADMIN) {
            throw new RuntimeException("");
        }
    }
}
