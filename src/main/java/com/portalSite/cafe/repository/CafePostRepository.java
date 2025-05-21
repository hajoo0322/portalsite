package com.portalSite.cafe.repository;

import com.portalSite.cafe.entity.CafePost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CafePostRepository extends JpaRepository<CafePost, Long>, CafePostRepositoryCustom {

    List<CafePost> findAllByCafeId(Long cafeId);

    @Query("SELECT cp FROM CafePost cp ORDER BY cp.id ASC")
    List<CafePost> cafePostFetchBatch(Pageable pageable);
}
