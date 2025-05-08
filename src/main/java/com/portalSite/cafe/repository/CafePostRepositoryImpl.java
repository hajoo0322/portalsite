package com.portalSite.cafe.repository;

import com.portalSite.cafe.dto.CafePostResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CafePostRepositoryImpl implements CafePostRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public Page<CafePostResponse> findAllByKeywordV2(
            String keyword, String writer,
            LocalDateTime createdAtStart, LocalDateTime createdAtEnd,
            boolean descending, Pageable pageable) {

        StringBuilder sql = new StringBuilder(
                "SELECT cp.id, cp.cafe_id, cp.cafe_board_id, cm.nickname, cp.title, cp.description " +
                        "FROM cafe_post cp JOIN cafe_member cm ON cp.cafe_member_id = cm.id " +
                        "WHERE (cp.title LIKE ?1 OR cp.description LIKE ?1)");

        appendSearchCondition(sql, writer, createdAtStart, createdAtEnd);

        if (descending) {
            sql.append(" ORDER BY cp.created_at DESC");
        }

        sql.append(" LIMIT ").append(pageable.getPageSize()).append(" OFFSET ").append(pageable.getOffset());

        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, keyword, writer, createdAtStart, createdAtEnd);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<CafePostResponse> content = rows
                .stream()
                .map(row -> {
                    return new CafePostResponse(
                            ((Long) ((Object[]) row)[0]),  // id
                            ((Long) ((Object[]) row)[1]),  // cafe_id
                            ((Long) ((Object[]) row)[2]),  // cafe_board_id
                            (String) ((Object[]) row)[3],  // nickname
                            (String) ((Object[]) row)[4],  // title
                            (String) ((Object[]) row)[5]   // description
                    );
                })
                .toList();

        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) " +
                        "FROM cafe_post cp JOIN cafe_member cm ON cp.cafe_member_id = cm.id " +
                        "WHERE (cp.title LIKE ?1 OR cp.description LIKE ?1)");

        appendSearchCondition(countSql, writer, createdAtStart, createdAtEnd);
        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        setParams(countQuery, keyword, writer, createdAtStart, createdAtEnd);

        long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<CafePostResponse> findAllByKeywordWithIndex(String keyword, Pageable pageable) {
        String sql = "SELECT cp.id, cp.cafe_id, cp.cafe_board_id, cm.nickname, cp.title, cp.description " +
                "FROM cafe_post cp JOIN cafe_member cm ON cm.id=cp.cafe_member_id " +
                "WHERE MATCH(title, description) AGAINST(?1 IN BOOLEAN MODE) " +
                "LIMIT ?2 OFFSET ?3";

        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager
                .createNativeQuery(sql)
                .setParameter(1, keyword + "*")
                .setParameter(2, pageable.getPageSize())
                .setParameter(3, pageable.getOffset())
                .getResultList();

        List<CafePostResponse> content = rows
                .stream()
                .map(row -> {
                    return new CafePostResponse(
                            ((Long) ((Object[]) row)[0]),  // id
                            ((Long) ((Object[]) row)[1]),  // cafe_id
                            ((Long) ((Object[]) row)[2]),  // cafe_board_id
                            (String) ((Object[]) row)[3],  // nickname
                            (String) ((Object[]) row)[4],  // title
                            (String) ((Object[]) row)[5]   // description
                    );
                })
                .toList();

        String countSql = "SELECT COUNT(*) FROM cafe_post cp WHERE MATCH(cp.title, cp.description) AGAINST (?1 IN BOOLEAN MODE)";

        long total = ((Long) entityManager.createNativeQuery(countSql)
                .setParameter(1, keyword + "*")
                .getSingleResult());

        return new PageImpl<>(content, pageable, total);
    }

    private void appendSearchCondition(
            StringBuilder sql, String writer, LocalDateTime createdAtStart, LocalDateTime createdAtEnd) {
        int index = 1;

        if (writer != null && !writer.isBlank()) {
            sql.append(" AND cm.nickname LIKE ?").append(++index);
        }
        if (createdAtStart != null) {
            sql.append(" AND cp.createdAt >= ?").append(++index);
        }
        if (createdAtEnd != null) {
            sql.append(" AND cp.createdAt <= ?").append(++index);
        }
    }

    private void setParams(
            Query query, String keyword, String writer, LocalDateTime createdAtStart, LocalDateTime createdAtEnd) {
        int index = 0;

        query.setParameter(++index, "%" + keyword + "%");

        if (writer != null && !writer.isBlank()) {
            query.setParameter(++index, "%" + writer + "%");
        }
        if (createdAtStart != null) {
            query.setParameter(++index, createdAtStart);
        }
        if (createdAtEnd != null) {
            query.setParameter(++index, createdAtEnd);
        }
    }
}
