package com.portalSite.news.repository;

import com.portalSite.news.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface NewsRepositoryCustom {

    Page<News> findAllByKeyword(String keyword, String writer,
                                LocalDateTime createdAtStart, LocalDateTime createdAtEnd,
                                boolean descending, Pageable pageable);
}
