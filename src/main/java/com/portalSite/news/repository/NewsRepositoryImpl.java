package com.portalSite.news.repository;

import com.portalSite.news.dto.response.NewsResponse;
import com.portalSite.news.entity.News;
import com.portalSite.news.entity.QNews;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class NewsRepositoryImpl implements NewsRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

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
}
