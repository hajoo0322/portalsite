package com.portalSite.blog.repository;

import com.portalSite.blog.entity.BlogPost;
import com.portalSite.blog.entity.QBlogPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class BlogPostRepositoryImpl implements BlogPostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BlogPost> findAllByKeyword(String keyword, Pageable pageable) {
        QBlogPost blogPost = QBlogPost.blogPost;

        List<BlogPost> result = queryFactory
                .selectFrom(blogPost)
                .where(blogPost.title.containsIgnoreCase(keyword)
                        .or(blogPost.description.containsIgnoreCase(keyword)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long title = queryFactory
                .select(blogPost.count())
                .from(blogPost)
                .where(blogPost.title.containsIgnoreCase(keyword)
                        .or(blogPost.description.containsIgnoreCase(keyword)))
                .fetchOne();

        return new PageImpl<>(result, pageable, title != null ? title : 0);
    }
}
