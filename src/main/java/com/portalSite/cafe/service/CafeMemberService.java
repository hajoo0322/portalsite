package com.portalSite.cafe.service;

import com.portalSite.cafe.dto.CafeMemberRequest;
import com.portalSite.cafe.dto.CafeMemberResponse;
import com.portalSite.cafe.entity.Cafe;
import com.portalSite.cafe.entity.CafeLevel;
import com.portalSite.cafe.entity.CafeMember;
import com.portalSite.cafe.repository.CafeLevelRepository;
import com.portalSite.cafe.repository.CafeMemberRepository;
import com.portalSite.cafe.repository.CafeRepository;
import com.portalSite.common.exception.core.DuplicateNameException;
import com.portalSite.common.exception.core.NotFoundException;
import com.portalSite.common.exception.custom.ErrorCode;
import com.portalSite.member.entity.Member;
import com.portalSite.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CafeMemberService {

    private final CafeMemberRepository cafeMemberRepository;
    private final CafeRepository cafeRepository;
    private final MemberRepository memberRepository;
    private final CafeLevelRepository cafeLevelRepository;

    @Transactional
    public CafeMemberResponse addCafeMember(CafeMemberRequest cafeMemberRequest, Long memberId, Long cafeId) {
        if (cafeMemberRepository.existsByNickname(cafeMemberRequest.nickname())) {
            throw new DuplicateNameException(ErrorCode.DUPLICATE_NAME);
        }
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        CafeLevel firstCafeLevel = cafeLevelRepository.findFirstByCafeIdOrderByGradeOrderAsc(cafeId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_LEVEL_NOT_FOUND));
        CafeMember cafeMember = CafeMember.of(cafe, member, firstCafeLevel, cafeMemberRequest.nickname());
        CafeMember savedCafeMember = cafeMemberRepository.save(cafeMember);
        return CafeMemberResponse.from(savedCafeMember);
    }

    @Transactional(readOnly = true)
    public CafeMemberResponse getCafeMember(Long memberId, Long cafeId) {
        CafeMember cafeMember = cafeMemberRepository.findByCafeIdAndMemberId(cafeId, memberId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_MEMBER_NOT_FOUND));
        return CafeMemberResponse.from(cafeMember);
    }

    @Transactional(readOnly = true)
    public List<CafeMemberResponse> getAllCafeMember(Long cafeId) {
        return cafeMemberRepository.findAllByCafeId(cafeId).stream().map(CafeMemberResponse::from).toList();
    }

    @Transactional
    public CafeMemberResponse updateCafeMember(CafeMemberRequest cafeMemberRequest, Long memberId, Long cafeId) {
        CafeMember cafeMember = cafeMemberRepository.findByCafeIdAndMemberId(cafeId, memberId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_MEMBER_NOT_FOUND));
        cafeMember.update(cafeMemberRequest);
        CafeMember savedCafeMember = cafeMemberRepository.save(cafeMember);
        return CafeMemberResponse.from(savedCafeMember);
    }

    @Transactional
    public void deleteCafeMember(Long memberId, Long cafeId) {
        CafeMember cafeMember = cafeMemberRepository.findByCafeIdAndMemberId(cafeId, memberId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_MEMBER_NOT_FOUND));
        cafeMember.delete(true);
    }

    @Transactional
    public void upgradeOneCafeMemberGrade(Cafe cafe) {
        List<CafeLevel> cafeLevelList = cafeLevelRepository.findAllByCafeIdOrderByGradeOrderDesc(cafe.getId());
        List<CafeMember> cafeMemberList = cafeMemberRepository.findAllByCafeId(cafe.getId());
        List<CafeMember> membersToUpgradeList = new ArrayList<>();

        // 회원이 조건 만족하면 갱신할 수 있는 가장 높은 등급으로 변경(현재 등급보다 낮은 등급으로는 변동 x)
        for (CafeMember member : cafeMemberList) {
            // 등급별 조건 검사
            for (CafeLevel cafeLevel : cafeLevelList) {
                if (!cafeLevel.getAutoLevel()) {
                    continue;
                }

                Integer memberCafeLevel = member.getCafeLevel().getGradeOrder();
                if(null==memberCafeLevel) {
                    throw new RuntimeException(); /*TODO 예외: 존재하지 않는 등급*/
                }
                if (memberCafeLevel < cafeLevel.getGradeOrder()
                        && member.getVisitCount() >= cafeLevel.getLevelVisitCount()
                        && member.getPostCount() >= cafeLevel.getLevelPostCount()
                        && member.getCommentCount() >= cafeLevel.getLevelCommentCount()
                ) { // 조건 만족 & 멤버 현 등급 보다 높으면 등업
                    member.updateCafeGrade(cafeLevel);
                    membersToUpgradeList.add(member);
                    break;
                }
            }
        }
        if(!membersToUpgradeList.isEmpty()){
            cafeMemberRepository.saveAll(membersToUpgradeList);
        }
    }

    @Transactional
    public CafeMemberResponse addFirstCafeMember(Long memberId, Long cafeId, CafeMemberRequest cafeMemberRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_NOT_FOUND));
        CafeLevel cafeLevel = cafeLevelRepository.findFirstByCafeIdOrderByGradeOrderDesc(cafeId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_LEVEL_NOT_FOUND));
        CafeMember cafeMember = CafeMember.of(cafe, member, cafeLevel, cafeMemberRequest.nickname());
        CafeMember savedCafeMember = cafeMemberRepository.save(cafeMember);
        return CafeMemberResponse.from(savedCafeMember);
    }

    public void duplicateCafeMemberName(String nickname) {
        cafeMemberRepository.findByNickname(nickname).ifPresent(cafeMember -> {
            throw new DuplicateNameException(ErrorCode.DUPLICATE_NAME);
        });
    }
}
