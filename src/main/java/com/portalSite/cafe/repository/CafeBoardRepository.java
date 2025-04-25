package com.portalSite.cafe.repository;

import com.portalSite.cafe.entity.CafeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeBoardRepository extends JpaRepository<CafeBoard,Long> {
    List<CafeBoard> findAllByCafeId(Long cafeId);
}
