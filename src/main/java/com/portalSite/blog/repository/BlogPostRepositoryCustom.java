package com.portalSite.blog.repository;

import com.portalSite.blog.dto.response.BlogPostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface BlogPostRepositoryCustom {

    Page<BlogPostResponse> findAllByKeywordV2(
            String keyword, String writer, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, Pageable pageable);

    Page<BlogPostResponse> findAllByKeywordWithIndex(String keyword, Pageable pageable);
}
