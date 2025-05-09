package com.portalSite.news.repository;

import com.portalSite.news.dto.response.NewsResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.List;

@RequiredArgsConstructor
public class NewsRepositoryImpl implements NewsRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public Page<NewsResponse> findAllByKeywordV2(
            String keyword, String writer,
            LocalDateTime createdAtStart, LocalDateTime createdAtEnd,
            boolean descending, Pageable pageable) {

        StringBuilder sql = new StringBuilder(
                "SELECT n.news_id, m.name, n.news_category_id, n.news_title, n.description, n.created_at " +
                        "FROM news n JOIN member m on n.member_id = m.member_id " +
                        "WHERE MATCH(n.news_title, n.description) AGAINST (?1 IN BOOLEAN MODE)");

        appendSearchCondition(sql, writer, createdAtStart, createdAtEnd);

        sql.append(" LIMIT ").append(pageable.getPageSize()).append(" OFFSET ").append(pageable.getOffset());

        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, keyword, writer, createdAtStart, createdAtEnd);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<NewsResponse> content = rows
                .stream()
                .map(row -> {
                    return new NewsResponse(
                            ((Long) ((Object[]) row)[0]),          // id
                            (String) ((Object[]) row)[1],          // name
                            ((Long) ((Object[]) row)[2]),          // news_category_id
                            (String) ((Object[]) row)[3],          // title
                            (String) ((Object[]) row)[4],          // description
                            ((Timestamp) ((Object[]) row)[5]).toLocalDateTime()  // created_at
                    );
                })
                .toList();

        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) " +
                "FROM news n JOIN member m on n.member_id = m.member_id " +
                "WHERE MATCH(n.news_title, n.description) AGAINST (?1 IN BOOLEAN MODE)");

        appendSearchCondition(countSql, writer, createdAtStart, createdAtEnd);
        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        setParams(countQuery, keyword, writer, createdAtStart, createdAtEnd);

        long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<NewsResponse> findAllByKeywordWithIndex(String keyword, Pageable pageable) {
        String sql = "SELECT n.news_id, m.name, n.news_category_id, n.news_title, n.description, n.created_at " +
                "FROM news n JOIN member m ON m.member_id=n.member_id " +
                "WHERE MATCH(n.news_title, n.description) AGAINST (?1 IN BOOLEAN MODE) " +
                "LIMIT ?2 OFFSET ?3";

        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager
                .createNativeQuery(sql)
                .setParameter(1, keyword + "*")
                .setParameter(2, pageable.getPageSize())
                .setParameter(3, pageable.getOffset())
                .getResultList();

        List<NewsResponse> content = rows
                .stream()
                .map(row -> {
                    return new NewsResponse(
                            ((Long) ((Object[]) row)[0]),          // id
                            (String) ((Object[]) row)[1],          // name
                            ((Long) ((Object[]) row)[2]),          // news_category_id
                            (String) ((Object[]) row)[3],          // title
                            (String) ((Object[]) row)[4],          // description
                            ((Timestamp) ((Object[]) row)[5]).toLocalDateTime()  // created_at
                    );
                })
                .toList();

        String countSql = "SELECT COUNT(*) FROM news n WHERE MATCH(n.news_title, n.description) AGAINST (?1 IN BOOLEAN MODE)";

        long total = ((Long) entityManager.createNativeQuery(countSql)
                .setParameter(1, keyword + "*")
                .getSingleResult());

        return new PageImpl<>(content, pageable, total);
    }

    private void appendSearchCondition(
            StringBuilder sql, String writer, LocalDateTime createdAtStart, LocalDateTime createdAtEnd) {
        int index = 1;

        if (writer != null && !writer.isBlank()) {
            sql.append(" AND MATCH(m.name) AGAINST (?").append(++index).append(" IN BOOLEAN MODE) ");
        }
        if (createdAtStart != null) {
            sql.append(" AND n.created_at >= ?").append(++index);
        }
        if (createdAtEnd != null) {
            sql.append(" AND n.created_at <= ?").append(++index);
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
