package com.portalSite.cafe.repository;

import com.portalSite.cafe.entity.CafeMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CafeMemberRepository extends JpaRepository<CafeMember, Long> {
    Optional<CafeMember> findByCafeIdAndMemberId(Long cafeId, Long memberId);

    List<CafeMember> findAllByCafeId(Long cafeId);

}
