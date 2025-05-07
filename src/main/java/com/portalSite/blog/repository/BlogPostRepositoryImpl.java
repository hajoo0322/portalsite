package com.portalSite.blog.repository;

import com.portalSite.blog.entity.BlogPost;
import com.portalSite.blog.entity.QBlogHashtag;
import com.portalSite.blog.entity.QBlogPost;
import com.portalSite.blog.entity.QHashtag;
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
        QBlogHashtag blogHashtag = QBlogHashtag.blogHashtag;
        QHashtag hashtag = QHashtag.hashtag;

        List<BlogPost> result = queryFactory
                .selectFrom(blogPost).distinct()
                .where(blogPost.title.containsIgnoreCase(keyword)
                        .or(blogPost.description.containsIgnoreCase(keyword)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = (long) queryFactory
                .selectFrom(blogPost).distinct()
                .where(blogPost.title.containsIgnoreCase(keyword)
                        .or(blogPost.description.containsIgnoreCase(keyword)))
                .fetch()
                .size();

//        List<BlogPost> result = queryFactory
//                .select(blogHashtag.post).distinct()
//                .from(blogHashtag)
//                .leftJoin(blogHashtag.post, blogPost)
//                .leftJoin(blogHashtag.hashtag, hashtag)
//                .where(blogPost.title.containsIgnoreCase(keyword)
//                        .or(blogPost.description.containsIgnoreCase(keyword))
//                        .or(hashtag.tag.containsIgnoreCase(keyword)))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        Long count = (long) queryFactory
//                .select(blogHashtag.post)
//                .from(blogHashtag)
//                .leftJoin(blogHashtag.post, blogPost)
//                .leftJoin(blogHashtag.hashtag, hashtag)
//                .where(blogPost.title.containsIgnoreCase(keyword)
//                        .or(blogPost.description.containsIgnoreCase(keyword))
//                        .or(hashtag.tag.containsIgnoreCase(keyword)))
//                .fetch()
//                .size();

        return new PageImpl<>(result, pageable, count != null ? count : 0);
    }
}
