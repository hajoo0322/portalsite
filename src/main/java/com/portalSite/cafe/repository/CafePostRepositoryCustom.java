package com.portalSite.cafe.repository;

import com.portalSite.cafe.dto.CafePostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface CafePostRepositoryCustom {

    Page<CafePostResponse> findAllByKeywordV2(
            String keyword, String writer, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, Pageable pageable);

    Page<CafePostResponse> findAllByKeywordWithIndex(String keyword, Pageable pageable);
}
