package com.portalSite.blog.repository;

import com.portalSite.blog.entity.BlogPost;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost,Long> {

    List<BlogPost> findAllByBlogId(Long blogboardId);

    List<BlogPost> findAllByBlogBoardId(Long blogboardId);
}
