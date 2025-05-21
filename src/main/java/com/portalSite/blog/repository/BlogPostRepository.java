package com.portalSite.blog.repository;

import com.portalSite.blog.entity.BlogPost;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long>, BlogPostRepositoryCustom {

    List<BlogPost> findAllByBlogId(Long blogboardId);

    List<BlogPost> findAllByBlogBoardId(Long blogboardId);

    @Query("SELECT bp FROM BlogPost bp ORDER BY bp.id ASC")
    List<BlogPost> blogPostFetchBatch(Pageable pageable);
}
