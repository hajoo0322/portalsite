package com.portalSite.blog.repository;

import com.portalSite.blog.dto.response.BlogPostResponse;
import com.portalSite.blog.entity.BlogPost;
import com.portalSite.blog.entity.QBlogPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class BlogPostRepositoryImpl implements BlogPostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private final EntityManager entityManager;
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

    @Override
    public Page<BlogPostResponse> findAllByKeywordV2(
            String keyword, String writer,
            LocalDateTime createdAtStart, LocalDateTime createdAtEnd,
            boolean descending, Pageable pageable) {
        int index;

        StringBuilder sql = new StringBuilder(
                "SELECT bp.id, bp.member_id, bp.blog_board_id, bp.title, bp.description " +
                "FROM blog_post bp " +
                "JOIN member m ON bp.member_id = m.member_id " +
                "WHERE (bp.title LIKE ?1 OR bp.description LIKE ?1)");

        index = appendSearchCondition(sql, writer, createdAtStart, createdAtEnd); // 4

        if (descending) {
            sql.append(" ORDER BY bp.created_at DESC");
        }

        sql.append(" LIMIT ?").append(++index).append(" OFFSET ?").append(++index); // 5, 6

        Query query = entityManager.createNativeQuery(sql.toString());
        index = setParams(query, keyword, writer, createdAtStart, createdAtEnd); // 4

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query
                .setParameter(++index, pageable.getPageSize()) // 5
                .setParameter(++index, pageable.getOffset()) // 6
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

        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) " +
                "FROM blog_post bp " +
                "JOIN member m ON bp.member_id = m.member_id " +
                "WHERE (bp.title LIKE ?1 OR bp.description LIKE ?1)");

        appendSearchCondition(countSql, writer, createdAtStart, createdAtEnd);
        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        setParams(countQuery, keyword, writer, createdAtStart, createdAtEnd);

        long total = ((Number) countQuery
                .getSingleResult()).longValue();

        return new PageImpl<>(content, pageable, total);
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
//
//    private void setParameter(
//            StringBuilder sql,Query query, String keyword, String writer,
//            LocalDateTime createdAtStart, LocalDateTime createdAtEnd
//    ) {
//        int paramIndex = 1;
//
//        query.setParameter(paramIndex++, "%" + keyword + "%");
//        if (writer != null && !writer.isBlank()) {
//            sql.append(" AND m.name LIKE ?").append(paramIndex);
//            query.setParameter(paramIndex++, "%" + writer + "%");
//        }
//
//        if (createdAtStart != null) {
//            sql.append(" AND bp.created_at >= ?").append(paramIndex);
//            query.setParameter(paramIndex++, createdAtStart);
//        }
//        if (createdAtEnd != null) {
//            sql.append(" AND bp.created_at <= ?").append(paramIndex);
//            query.setParameter(paramIndex++, createdAtEnd);
//        }
//    }

    private int appendSearchCondition(
            StringBuilder sql, String writer, LocalDateTime createdAtStart, LocalDateTime createdAtEnd) {
        int index = 1;

        if (writer != null && !writer.isBlank()) {
            sql.append(" AND m.name LIKE ?").append(++index); // 2
        }
        if (createdAtStart != null) {
            sql.append(" AND bp.created_at >= ?").append(++index); // 3
        }
        if (createdAtEnd != null) {
            sql.append(" AND bp.created_at <= ?").append(++index); // 4
        }

        return index; // 4
    }

    private int setParams(
            Query query, String keyword, String writer, LocalDateTime createdAtStart, LocalDateTime createdAtEnd) {
        int index = 0;

        query.setParameter(++index, "%" + keyword + "%"); // 1

        if (writer!= null && !writer.isBlank()) {
            query.setParameter(++index, "%" + writer + "%"); // 2
        }
        if (createdAtStart != null) {
            query.setParameter(++index, createdAtStart); // 3
        }
        if (createdAtEnd != null) {
            query.setParameter(++index, createdAtEnd); // 4
        }

        return index; // 4
    }
}
