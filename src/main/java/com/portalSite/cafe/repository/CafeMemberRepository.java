package com.portalSite.cafe.repository;

import com.portalSite.cafe.entity.CafeMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CafeMemberRepository extends JpaRepository<CafeMember, Long> {
    Optional<CafeMember> findByCafeIdAndMemberId(Long cafeId, Long memberId);

    List<CafeMember> findAllByCafeId(Long cafeId);

    Optional<CafeMember> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    @Query("SELECT cm FROM CafeMember cm WHERE cm.id=:cafeId AND cm.isDeleted=false")
    List<CafeMember> findAllByCafeIdIsDeletedFalse(@Param("cafeId")Long cafeId);
}
