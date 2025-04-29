package com.portalSite.cafe.repository;

import com.portalSite.cafe.entity.CafePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CafePostRepository extends JpaRepository<CafePost, Long> {

    List<CafePost> findAllByCafeId(Long cafeId);

    @Query("""
            SELECT cp
            FROM CafePost cp
            WHERE cp.title LIKE %:keyword%
                OR cp.description LIKE %:keyword%
            """)
    List<CafePost> findAllByKeyword(@Param("keyword") String keyword);
}
