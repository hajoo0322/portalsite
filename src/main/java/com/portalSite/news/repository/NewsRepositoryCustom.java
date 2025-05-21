package com.portalSite.news.repository;

import com.portalSite.news.dto.response.NewsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface NewsRepositoryCustom {

    Page<NewsResponse> findAllByKeywordV2(
            String keyword, String writer, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, Pageable pageable);

    Page<NewsResponse> findAllByKeywordWithIndex(String keyword, Pageable pageable);
}
