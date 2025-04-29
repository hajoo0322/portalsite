package com.portalSite.like.repository;

import com.portalSite.cafe.entity.CafePost;
import com.portalSite.like.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<PostLike, Long> {
    Integer countByCafePost(CafePost cafePost);
}
