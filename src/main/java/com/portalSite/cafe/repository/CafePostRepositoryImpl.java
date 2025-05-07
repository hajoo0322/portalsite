package com.portalSite.cafe.repository;

import com.portalSite.cafe.dto.CafePostResponse;
import com.portalSite.cafe.entity.CafePost;
import com.portalSite.cafe.entity.QCafePost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
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
    public Page<CafePostResponse> findAllByKeywordWithIndex(String keyword, Pageable pageable) {
        String sql ="SELECT cp.id, cp.cafe_id, cp.cafe_board_id, cm.nickname, cp.title, cp.description " +
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
}
