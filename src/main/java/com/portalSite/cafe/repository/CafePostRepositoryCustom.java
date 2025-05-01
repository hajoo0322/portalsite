package com.portalSite.cafe.repository;

import com.portalSite.cafe.entity.CafePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CafePostRepositoryCustom {

    Page<CafePost> findAllByKeyword(String keyword, Pageable pageable);
}
