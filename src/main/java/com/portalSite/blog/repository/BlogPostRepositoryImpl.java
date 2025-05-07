package com.portalSite.blog.repository;

import com.portalSite.blog.dto.response.BlogPostResponse;
import com.portalSite.blog.entity.BlogPost;
import com.portalSite.blog.entity.QBlogPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.util.List;

@RequiredArgsConstructor
public class BlogPostRepositoryImpl implements BlogPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

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

        Long total = queryFactory
                .select(blogPost.count())
                .from(blogPost)
                .where(blogPost.title.containsIgnoreCase(keyword)
                        .or(blogPost.description.containsIgnoreCase(keyword)))
                .fetchOne();

        return new PageImpl<>(result, pageable, total != null ? total : 0);
    }

    @Override
    public Page<BlogPostResponse> findAllByKeywordWithIndex(String keyword, Pageable pageable) {
        String sql = "SELECT bp.id, bp.member_id, bp.blog_board_id, bp.title, bp.description " +
                "FROM blog_post bp " +
                "WHERE MATCH(bp.title, bp.description) AGAINST (?1 IN BOOLEAN MODE) " +
                "LIMIT ?2 OFFSET ?3";

        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager
                .createNativeQuery(sql)
                .setParameter(1, keyword + "*")
                .setParameter(2, pageable.getPageSize())
                .setParameter(3, pageable.getOffset())
                .getResultList();

        List<BlogPostResponse> content = rows
                .stream()
                .map(row -> {
                    return new BlogPostResponse(
                            ((Long) ((Object[]) row)[0]),  // id
                            ((Long) ((Object[]) row)[1]),  // blog_id
                            ((Long) ((Object[]) row)[2]),  // blog_board_id
                            (String) ((Object[]) row)[3],  // title
                            (String) ((Object[]) row)[4]   // description
                    );
                })
                .toList();

        String countSql = "SELECT COUNT(*) FROM blog_post bp WHERE MATCH(bp.title, bp.description) AGAINST (?1 IN BOOLEAN MODE)";

        long total = ((Long) entityManager.createNativeQuery(countSql)
                .setParameter(1, keyword + "*")
                .getSingleResult());

        return new PageImpl<>(content, pageable, total);
    }
}
