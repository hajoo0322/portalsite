package com.portalSite.cafe.repository;

import com.portalSite.cafe.dto.CafePostResponse;
import com.portalSite.cafe.entity.CafePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

public interface CafePostRepositoryCustom {

    Page<CafePost> findAllByKeyword(String keyword, Pageable pageable);

    Page<CafePostResponse> findAllByKeywordV2(
            String keyword, String writer, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, Pageable pageable);

    Slice<CafePostResponse> findAllByKeywordWithIndex(String keyword, Pageable pageable);
}
