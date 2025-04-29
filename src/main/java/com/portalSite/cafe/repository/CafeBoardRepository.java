package com.portalSite.cafe.repository;

import com.portalSite.cafe.entity.CafeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CafeBoardRepository extends JpaRepository<CafeBoard,Long> {
    List<CafeBoard> findAllByCafeId(Long cafeId);

    Optional<CafeBoard> findByBoardNameAndCafeId(String boardName,Long cafeId);

    boolean existsByBoardName(String boardName);

}
