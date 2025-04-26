package com.portalSite.comment.repository;

import com.portalSite.blog.entity.Blog;
import com.portalSite.cafe.entity.CafePost;
import com.portalSite.comment.entity.Comment;
import com.portalSite.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBlog(Blog blog);
    List<Comment> findAllByNews(News news);
    List<Comment> findAllByCafePost(CafePost cafePost);
}
