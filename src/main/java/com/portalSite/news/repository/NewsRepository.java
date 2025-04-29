package com.portalSite.news.repository;

import com.portalSite.news.entity.News;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NewsRepository extends JpaRepository<News, Long> {

  List<News> findAllByNewsCategoryId(Long categoryId);

  @Query("""
""")
  List<News> findAllByKeyword(String keyword);
}
