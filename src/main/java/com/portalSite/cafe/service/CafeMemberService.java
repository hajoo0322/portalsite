package com.portalSite.cafe.service;

import com.portalSite.cafe.dto.CafeMemberRequest;
import com.portalSite.cafe.dto.CafeMemberResponse;
import com.portalSite.cafe.entity.Cafe;
import com.portalSite.cafe.entity.CafeLevel;
import com.portalSite.cafe.entity.CafeMember;
import com.portalSite.cafe.repository.CafeLevelRepository;
import com.portalSite.cafe.repository.CafeMemberRepository;
import com.portalSite.cafe.repository.CafeRepository;
import com.portalSite.member.entity.Member;
import com.portalSite.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeMemberService {

    private final CafeMemberRepository cafeMemberRepository;
    private final CafeRepository cafeRepository;
    private final MemberRepository memberRepository;
    private final CafeLevelRepository cafeLevelRepository;

    @Transactional
    public CafeMemberResponse addCafeMember(CafeMemberRequest cafeMemberRequest, Long memberId, Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new RuntimeException(""));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException(""));
        CafeLevel firstCafeLevel = cafeLevelRepository.findFirstByCafeIdOrderByGradeOrderAsc(cafeId).orElseThrow(() -> new RuntimeException(""));
        CafeMember cafeMember = CafeMember.of(cafe, member, firstCafeLevel.getGrade(), cafeMemberRequest.nickname());
        return CafeMemberResponse.from(cafeMember);
    }

    @Transactional(readOnly = true)
    public CafeMemberResponse getCafeMember(Long memberId, Long cafeId) {
        CafeMember cafeMember = cafeMemberRepository.findByCafeIdAndMemberId(cafeId, memberId).orElseThrow(() -> new RuntimeException(""));
        return CafeMemberResponse.from(cafeMember);
    }

    @Transactional(readOnly = true)
    public List<CafeMemberResponse> getAllCafeMember(Long cafeId) {
        List<CafeMember> cafeMemberList = cafeMemberRepository.findAllByCafeId(cafeId);
        if (cafeMemberList.isEmpty()) {
            throw new RuntimeException("");
        }
        return cafeMemberList.stream().map(CafeMemberResponse::from).toList();
    }

    @Transactional
    public CafeMemberResponse updateCafeMember(CafeMemberRequest cafeMemberRequest, Long memberId, Long cafeId) {
        CafeMember cafeMember = cafeMemberRepository.findByCafeIdAndMemberId(cafeId, memberId).orElseThrow(() -> new RuntimeException(""));
        cafeMember.update(cafeMemberRequest);
        CafeMember savedCafeMember = cafeMemberRepository.save(cafeMember);
        return CafeMemberResponse.from(savedCafeMember);
    }

    @Transactional
    public void deleteCafeMember(Long memberId, Long cafeId) {
        CafeMember cafeMember = cafeMemberRepository.findByCafeIdAndMemberId(cafeId, memberId).orElseThrow(() -> new RuntimeException(""));
        cafeMember.delete(true);
    }
}
