package com.portalSite.news.repository;

import com.portalSite.news.entity.News;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {

  List<News> findAllByNewsCategoryId(Long categoryId);
}
