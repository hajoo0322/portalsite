package com.portalSite.blog.repository;

import com.portalSite.blog.entity.BlogPost;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    List<BlogPost> findAllByBlogId(Long blogboardId);

    List<BlogPost> findAllByBlogBoardId(Long blogboardId);

    @Query("""
            SELECT bp
            FROM BlogPost bp
            WHERE bp.title LIKE %:keyword%
                OR bp.description LIKE %:keyword%
            """)
    Page<BlogPost> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
