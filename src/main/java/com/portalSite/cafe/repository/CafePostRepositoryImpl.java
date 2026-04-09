package com.portalSite.cafe.repository;

import com.portalSite.cafe.dto.CafePostResponse;
import com.portalSite.cafe.entity.CafePost;
import com.portalSite.cafe.entity.QCafePost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CafePostRepositoryImpl implements CafePostRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public Page<CafePost> findAllByKeyword(String keyword, Pageable pageable) {
        QCafePost cafePost = QCafePost.cafePost;

        List<CafePost> responses = queryFactory
                .selectFrom(cafePost)
                .where(cafePost.title.containsIgnoreCase(keyword)
                        .or(cafePost.description.containsIgnoreCase(keyword)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(cafePost.count())
                .from(cafePost)
                .where(cafePost.title.containsIgnoreCase(keyword)
                        .or(cafePost.description.containsIgnoreCase(keyword)))
                .fetchOne();

        return new PageImpl<>(responses, pageable, total != null ? total : 0);
    }


    @Override
    public Page<CafePostResponse> findAllByKeywordV2(
            String keyword, String writer, LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd, Pageable pageable) {

        StringBuilder sql = new StringBuilder(
                "SELECT cp.id, cp.cafe_id, cp.cafe_board_id, cm.nickname, cp.title, cp.description " +
                        "FROM cafe_post cp JOIN cafe_member cm ON cp.cafe_member_id = cm.id " +
                        "WHERE MATCH(cp.title, cp.description) AGAINST (?1 IN BOOLEAN MODE)");

        appendSearchCondition(sql, writer, createdAtStart, createdAtEnd);

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
                "WHERE MATCH(cp.title, cp.description) AGAINST (?1 IN BOOLEAN MODE)");

        appendSearchCondition(countSql, writer, createdAtStart, createdAtEnd);
        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        setParams(countQuery, keyword, writer, createdAtStart, createdAtEnd);

        long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Slice<CafePostResponse> findAllByKeywordWithIndex(String keyword, Pageable pageable) {
        String idsSql = "SELECT cp.id " +
                "FROM cafe_post cp " +
                "WHERE MATCH(title, description) AGAINST(?1 IN BOOLEAN MODE) " +
                "LIMIT ?2 OFFSET ?3";

        @SuppressWarnings("unchecked")
        List<Long> ids = entityManager
                .createNativeQuery(idsSql)
                .setParameter(1, keyword + "*")
                .setParameter(2, pageable.getPageSize()+1)
                .setParameter(3, pageable.getOffset())
                .getResultList();

        boolean hasNext = ids.size() > pageable.getPageSize();

        if(ids.isEmpty()){
            return new SliceImpl<>(List.of(), pageable, hasNext);
        }

        List<Long> pageIds = ids.stream().limit(pageable.getPageSize()).toList();

        String contentsSql = "SELECT cp.id, cp.cafe_id, cp.cafe_board_id, cm.nickname, cp.title, cp.description " +
                "FROM cafe_post cp JOIN cafe_member cm ON cm.id=cp.cafe_member_id " +
                "WHERE cp.id IN (:ids) " +
                "ORDER BY cp.id DESC";

        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager
                .createNativeQuery(contentsSql)
                .setParameter("ids", pageIds)
                .getResultList();

        List<CafePostResponse> content = rows.stream()
                .map(row -> {
                    return new CafePostResponse(
                            (Long) ((Object[]) row)[0],  // id
                            (Long) ((Object[]) row)[1],  // cafe_id
                            (Long) ((Object[]) row)[2],  // cafe_board_id
                            (String) ((Object[]) row)[3],  // nickname
                            (String) ((Object[]) row)[4],  // title
                            (String) ((Object[]) row)[5]   // description
                    );
                })
                .toList();

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private void appendSearchCondition(
            StringBuilder sql, String writer, LocalDateTime createdAtStart, LocalDateTime createdAtEnd) {
        int index = 1;

        if (writer != null && !writer.isBlank()) {
            sql.append(" AND MATCH(cm.nickname) AGAINST (?").append(++index).append(" IN BOOLEAN MODE) ");
        }
        if (createdAtStart != null) {
            sql.append(" AND cp.created_at >= ?").append(++index);
        }
        if (createdAtEnd != null) {
            sql.append(" AND cp.created_at <= ?").append(++index);
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
