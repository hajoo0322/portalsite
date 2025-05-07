package com.portalSite.blog.repository;

import com.portalSite.blog.entity.BlogPost;
import com.portalSite.blog.entity.QBlogPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class BlogPostRepositoryImpl implements BlogPostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BlogPost> findAllByKeyword(
            String keyword, String writer,
            LocalDateTime createdAtStart, LocalDateTime createdAtEnd,
            boolean descending, Pageable pageable) {

        QBlogPost blogPost = QBlogPost.blogPost;
//        QBlogHashtag blogHashtag = QBlogHashtag.blogHashtag;`
//        QHashtag hashtag = QHashtag.hashtag;

        BooleanBuilder builder = new BooleanBuilder();

        //기본 검색 조건(타이틀, 내용)
        builder.and(blogPost.title.containsIgnoreCase(keyword))
                .or(blogPost.description.containsIgnoreCase(keyword));

        //작성자 필터
        if (writer != null && !writer.isBlank()) {
            builder.and(blogPost.member.name.containsIgnoreCase(writer));
        }

        //날짜 필터
        if (createdAtStart != null) {
            builder.and(blogPost.createdAt.goe(createdAtStart));
        }
        if (createdAtEnd != null) {
            builder.and(blogPost.createdAt.loe(createdAtEnd));
        }

        // 정렬 조건 설정
        OrderSpecifier<?> order = descending ?
                blogPost.createdAt.desc() : blogPost.createdAt.asc();

        List<BlogPost> result = queryFactory
                .selectFrom(blogPost).distinct()
                .where(builder)
                .orderBy(order)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(blogPost.countDistinct())
                .from(blogPost)
                .where(builder)
                .fetchOne();

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
