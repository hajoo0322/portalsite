package com.portalSite.cafe.repository;

import com.portalSite.cafe.entity.CafePost;
import com.portalSite.cafe.entity.QCafePost;
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
public class CafePostRepositoryImpl implements CafePostRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CafePost> findAllByKeyword(
            String keyword, String writer,
            LocalDateTime createdAtStart, LocalDateTime createdAtEnd,
            boolean descending, Pageable pageable) {

        QCafePost cafePost = QCafePost.cafePost;

        BooleanBuilder builder = new BooleanBuilder();

        // 기본 검색 조건(타이틀, 내용)
        builder.and(cafePost.title.containsIgnoreCase(keyword))
                .or(cafePost.description.containsIgnoreCase(keyword));

        // 작성자 필터
        if (writer != null && !writer.isBlank()) {
            builder.and(cafePost.cafeMember.nickname.containsIgnoreCase(writer));
        }

        // 날짜 필터
        if (createdAtStart != null) {
            builder.and(cafePost.createdAt.goe(createdAtStart));
        }
        if (createdAtEnd != null) {
            builder.and(cafePost.createdAt.loe(createdAtEnd));
        }

        // 정렬 조건 설정
        OrderSpecifier<?> order = descending ?
                cafePost.createdAt.desc() : cafePost.createdAt.asc();

        List<CafePost> cafePosts = queryFactory
                .selectFrom(cafePost).distinct()
                .where(builder)
                .orderBy(order)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(cafePost.countDistinct())
                .from(cafePost)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(cafePosts, pageable, total != null ? total : 0);
    }
}
