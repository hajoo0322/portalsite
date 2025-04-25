package com.portalSite.cafe.repository;

import com.portalSite.cafe.entity.CafeLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeLevelRepository extends JpaRepository<CafeLevel, Long> {
    List<CafeLevel> findAllByCafeId(Long cafeId);
}
