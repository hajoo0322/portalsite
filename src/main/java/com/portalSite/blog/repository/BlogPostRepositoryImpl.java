package com.portalSite.blog.repository;

import com.portalSite.blog.dto.response.BlogPostResponse;
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
    private final EntityManager entityManager;

    @Override
    public Page<BlogPostResponse> findAllByKeywordV2(
            String keyword, String writer,
            LocalDateTime createdAtStart, LocalDateTime createdAtEnd,
            boolean descending, Pageable pageable) {

        StringBuilder sql = new StringBuilder(
                "SELECT bp.id, bp.member_id, bp.blog_board_id, bp.title, bp.description " +
                        "FROM blog_post bp JOIN member m ON bp.member_id = m.member_id " +
                        "WHERE MATCH(bp.title, bp.description) AGAINST (?1 IN BOOLEAN MODE)");

        appendSearchCondition(sql, writer, createdAtStart, createdAtEnd);

        sql.append(" LIMIT ").append(pageable.getPageSize()).append(" OFFSET ").append(pageable.getOffset());

        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, keyword, writer, createdAtStart, createdAtEnd);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

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
                "FROM blog_post bp JOIN member m ON bp.member_id = m.member_id " +
                "WHERE MATCH(bp.title, bp.description) AGAINST (?1 IN BOOLEAN MODE)");

        appendSearchCondition(countSql, writer, createdAtStart, createdAtEnd);
        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        setParams(countQuery, keyword, writer, createdAtStart, createdAtEnd);

        long total = ((Number) countQuery.getSingleResult()).longValue();

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

    private void appendSearchCondition(
            StringBuilder sql, String writer, LocalDateTime createdAtStart, LocalDateTime createdAtEnd) {
        int index = 1;

        if (writer != null && !writer.isBlank()) {
            sql.append(" AND MATCH (m.name) AGAINST (?").append(++index).append(" IN BOOLEAN MODE) ");
        }
        if (createdAtStart != null) {
            sql.append(" AND bp.created_at >= ?").append(++index);
        }
        if (createdAtEnd != null) {
            sql.append(" AND bp.created_at <= ?").append(++index);
        }
    }

    private void setParams(
            Query query, String keyword, String writer, LocalDateTime createdAtStart, LocalDateTime createdAtEnd) {
        int index = 0;

        query.setParameter(++index, keyword + "*");

        if (writer != null && !writer.isBlank()) {
            query.setParameter(++index, writer + "*");
        }
        if (createdAtStart != null) {
            query.setParameter(++index, createdAtStart);
        }
        if (createdAtEnd != null) {
            query.setParameter(++index, createdAtEnd);
        }
    }
}
