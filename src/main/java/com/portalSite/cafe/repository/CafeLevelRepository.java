package com.portalSite.cafe.repository;

import com.portalSite.cafe.entity.CafeLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CafeLevelRepository extends JpaRepository<CafeLevel, Long> {
    List<CafeLevel> findAllByCafeId(Long cafeId);

    Optional<CafeLevel> findFirstByCafeIdOrderByGradeOrderAsc(Long cafeId);

    List<CafeLevel> findAllByCafeIdOrderByGradeOrderDesc(Long cafeId);

    Optional<CafeLevel> findFirstByCafeIdOrderByGradeOrderDesc(Long cafeId);
}
