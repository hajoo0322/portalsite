package com.portalSite.blog.repository;

import com.portalSite.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog,Long> {
}
