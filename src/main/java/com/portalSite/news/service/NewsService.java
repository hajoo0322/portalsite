package com.portalSite.news.service;

import com.portalSite.common.exception.custom.CustomException;
import com.portalSite.common.exception.custom.ErrorCode;
import com.portalSite.member.entity.Member;
import com.portalSite.member.repository.MemberRepository;
import com.portalSite.news.dto.request.NewsCreateRequest;
import com.portalSite.news.dto.request.NewsRequest;
import com.portalSite.news.entity.News;
import com.portalSite.news.entity.NewsCategory;
import com.portalSite.news.repository.NewsCategoryRepository;
import com.portalSite.news.repository.NewsRepository;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final MemberRepository memberRepository;
    private final NewsCategoryRepository newsCategoryRepository;

    @Transactional
    public News createNews(NewsCreateRequest requestDto, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.AUTHOR_NOT_FOUND)
        );

        NewsCategory newsCategory = newsCategoryRepository.findById(requestDto.categoryId())
                .orElseThrow(
                        () -> new CustomException(ErrorCode.NEWS_CATEGORY_NOT_FOUND)
                );

        News news = News.of(member, newsCategory, requestDto.newsTitle(), requestDto.description());

        return newsRepository.save(news);
    }

    @Transactional(readOnly = true)
    public News getNewsById(Long newsId) {
        return newsRepository.findById(newsId).orElseThrow(
                () -> new CustomException(ErrorCode.NEWS_NOT_FOUND)
        );
    }

    @Transactional(readOnly = true)
    public List<News> getNewsListByCategory(Long categoryId, Pageable pageable) {
        return newsRepository.findAllByNewsCategoryId(categoryId, pageable);
    }

    @Transactional
    public News updateNews(NewsRequest requestDto, Long newsId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () ->new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );

        News news = getNewsById(newsId);
        if (!memberId.equals(news.getMember().getId())) {
            throw new CustomException(ErrorCode.NO_UPDATE_PERMISSION);
        }

        NewsCategory newsCategory = newsCategoryRepository.findById(requestDto.categoryId())
                .orElseThrow(
                        () -> new CustomException(ErrorCode.NEWS_CATEGORY_NOT_FOUND)
                );

        news.updateNews(newsCategory, requestDto.newsTitle(), requestDto.description());

        return news;
    }

    @Transactional
    public void deleteNews(Long newsId, Long memberId) {
        News news = getNewsById(newsId);
        if (!memberId.equals(news.getMember().getId())) {
            throw new CustomException(ErrorCode.NO_DELETE_PERMISSION);
        }

        newsRepository.delete(news);
    }
}
