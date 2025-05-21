package com.portalSite.news.repository;

import com.portalSite.news.entity.News;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NewsRepository extends JpaRepository<News, Long>, NewsRepositoryCustom {

    List<News> findAllByNewsCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT n FROM News n ORDER BY n.id ASC")
    List<News> newsFetchBatch(Pageable pageable);
}
