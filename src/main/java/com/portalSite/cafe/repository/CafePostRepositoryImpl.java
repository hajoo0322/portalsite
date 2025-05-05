package com.portalSite.cafe.repository;

import com.portalSite.cafe.entity.CafePost;
import com.portalSite.cafe.entity.QCafePost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class CafePostRepositoryImpl implements CafePostRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CafePost> findAllByKeyword(String keyword, Pageable pageable) {
        QCafePost cafePost = QCafePost.cafePost;

        List<CafePost> cafePosts = queryFactory
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

        return new PageImpl<>(cafePosts, pageable, total != null ? total : 0);
    }
}
