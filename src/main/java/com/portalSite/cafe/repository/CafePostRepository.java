package com.portalSite.cafe.repository;

import com.portalSite.cafe.entity.CafePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafePostRepository extends JpaRepository<CafePost, Long> {

    List<CafePost> findAllByCafeId(Long cafeId);
}
