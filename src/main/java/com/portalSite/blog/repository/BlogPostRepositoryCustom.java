package com.portalSite.blog.repository;

import com.portalSite.blog.entity.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogPostRepositoryCustom {

    Page<BlogPost> findAllByKeyword(String keyword, Pageable pageable);
}
