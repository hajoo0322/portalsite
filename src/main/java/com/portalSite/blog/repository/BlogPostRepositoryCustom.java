package com.portalSite.blog.repository;

import com.portalSite.blog.entity.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface BlogPostRepositoryCustom {

    Page<BlogPost> findAllByKeyword(String keyword, String writer,
                                    LocalDateTime createdAtStart, LocalDateTime createdAtEnd,
                                    boolean descending, Pageable pageable);
}
