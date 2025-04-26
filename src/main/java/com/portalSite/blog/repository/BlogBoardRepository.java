package com.portalSite.blog.repository;

import com.portalSite.blog.entity.BlogBoard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogBoardRepository extends JpaRepository<BlogBoard,Long> {

    List<BlogBoard> findAllByBlogId(Long blogId);
}
