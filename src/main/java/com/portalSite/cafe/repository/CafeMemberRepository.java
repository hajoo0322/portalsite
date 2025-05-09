package com.portalSite.cafe.repository;

import com.portalSite.cafe.entity.CafeMember;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CafeMemberRepository extends JpaRepository<CafeMember, Long> {
    Optional<CafeMember> findByCafeIdAndMemberId(Long cafeId, Long memberId);

    List<CafeMember> findAllByCafeId(Long cafeId);

    Optional<CafeMember> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

}
