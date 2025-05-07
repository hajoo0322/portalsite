package com.portalSite.news.repository;

import com.portalSite.news.entity.News;
import com.portalSite.news.entity.QNews;
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
public class NewsRepositoryImpl implements NewsRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<News> findAllByKeyword(
            String keyword, String writer,
            LocalDateTime createdAtStart, LocalDateTime createdAtEnd,
            boolean descending, Pageable pageable) {

        QNews news = QNews.news;

        BooleanBuilder builder = new BooleanBuilder();

        // 기본 검색 조건(타이틀, 내용)
        builder.and(news.newsTitle.containsIgnoreCase(keyword))
                .or(news.description.containsIgnoreCase(keyword));

        // 작성자 필터
        if (createdAtStart != null) {
            builder.and(news.member.name.containsIgnoreCase(writer));
        }

        // 날짜 필터
        if (createdAtStart != null) {
            builder.and(news.createdAt.goe(createdAtStart));
        }
        if (createdAtEnd != null) {
            builder.and(news.createdAt.loe(createdAtEnd));
        }

        // 정렬 조건 설정
        OrderSpecifier<?> order = descending ?
                news.createdAt.desc() : news.createdAt.asc();

        List<News> result = queryFactory
                .selectFrom(news).distinct()
                .where(builder)
                .orderBy(order)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(news.countDistinct())
                .from(news)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(result, pageable, total != null ? total : 0);
    }
}
