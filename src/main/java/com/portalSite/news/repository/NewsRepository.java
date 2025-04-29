package com.portalSite.news.repository;

import com.portalSite.news.entity.News;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findAllByNewsCategoryId(Long categoryId);

    @Query("""
            SELECT n
            FROM News n
            WHERE n.newsTitle LIKE %:keyword%
                OR n.description LIKE %:keyword%
            """)
    List<News> findAllByKeyword(@Param("keyword") String keyword);
}
