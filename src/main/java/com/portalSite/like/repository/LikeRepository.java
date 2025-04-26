package com.portalSite.like.repository;

import com.portalSite.cafe.entity.CafePost;
import com.portalSite.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Integer countByCafePost(CafePost cafePost);
}
