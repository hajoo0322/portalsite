package com.portalSite.news.repository;

import com.portalSite.news.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsRepositoryCustom {

    Page<News> findAllByKeyword(String keyword, Pageable pageable);
}
