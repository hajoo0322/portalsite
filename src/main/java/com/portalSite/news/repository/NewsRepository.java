package com.portalSite.news.repository;

import com.portalSite.news.entity.News;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsRepository extends JpaRepository<News, Long>, NewsRepositoryCustom {

    List<News> findAllByNewsCategoryId(Long categoryId);
}
