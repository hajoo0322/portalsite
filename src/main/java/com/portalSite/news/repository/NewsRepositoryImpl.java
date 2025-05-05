package com.portalSite.news.repository;

import com.portalSite.news.entity.News;
import com.portalSite.news.entity.QNews;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class NewsRepositoryImpl implements NewsRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<News> findAllByKeyword(String keyword, Pageable pageable) {
        QNews news = QNews.news;

        List<News> result = queryFactory
                .selectFrom(news)
                .where(news.newsTitle.containsIgnoreCase(keyword).
                        or(news.description.containsIgnoreCase(keyword)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(news.count())
                .from(news)
                .where(
                        news.newsTitle.containsIgnoreCase(keyword)
                                .or(news.description.containsIgnoreCase(keyword))
                )
                .fetchOne();

        return new PageImpl<>(result, pageable, total != null ? total : 0);
    }
}
