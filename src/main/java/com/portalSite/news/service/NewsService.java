package com.portalSite.news.service;

import com.portalSite.common.exception.custom.CustomException;
import com.portalSite.common.exception.custom.ErrorCode;
import com.portalSite.member.entity.Member;
import com.portalSite.member.repository.MemberRepository;
import com.portalSite.news.dto.request.NewsCreateRequest;
import com.portalSite.news.dto.request.NewsRequest;
import com.portalSite.news.dto.response.NewsListResponse;
import com.portalSite.news.dto.response.NewsResponse;
import com.portalSite.news.entity.News;
import com.portalSite.news.entity.NewsCategory;
import com.portalSite.news.repository.NewsCategoryRepository;
import com.portalSite.news.repository.NewsRepository;

import java.util.List;

import com.portalSite.search.document.PostSearchDocument;
import com.portalSite.search.repository.PostSearchRepository;
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
    private final PostSearchRepository postSearchRepository;

    @Transactional
    public NewsResponse createNews(NewsCreateRequest requestDto, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.AUTHOR_NOT_FOUND)
        );

        NewsCategory newsCategory = newsCategoryRepository.findById(requestDto.categoryId())
                .orElseThrow(
                        () -> new CustomException(ErrorCode.NEWS_CATEGORY_NOT_FOUND)
                );

        News news = News.of(member, newsCategory, requestDto.newsTitle(), requestDto.description());
        News savedNews = newsRepository.save(news);

        PostSearchDocument document = PostSearchDocument.from(savedNews);
        postSearchRepository.save(document);

        return NewsResponse.from(savedNews);
    }

    @Transactional(readOnly = true)
    public NewsResponse getNewsById(Long newsId) {
        return NewsResponse.from(newsRepository.findById(newsId).orElseThrow(
                () -> new CustomException(ErrorCode.NEWS_NOT_FOUND)
        ));
    }

    @Transactional(readOnly = true)
    public NewsListResponse getNewsListByCategory(Long categoryId, Pageable pageable) {
        List<NewsResponse> newsList = newsRepository.findAllByNewsCategoryId(categoryId, pageable).stream()
                .map(NewsResponse::from)
                .toList();

        if (newsList.isEmpty()) {
            return NewsListResponse.from("해당 카테고리에 존재하는 뉴스가 없습니다.", null);
        }
        return NewsListResponse.from("뉴스 조회 성공", newsList);
    }

    @Transactional
    public NewsResponse updateNews(NewsRequest requestDto, Long newsId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () ->new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );

        News news = newsRepository.findById(newsId).orElseThrow(
                () -> new CustomException(ErrorCode.NEWS_NOT_FOUND)
        );

        if (!memberId.equals(news.getMember().getId())) {
            throw new CustomException(ErrorCode.NO_UPDATE_PERMISSION);
        }

        NewsCategory newsCategory = newsCategoryRepository.findById(requestDto.categoryId())
                .orElseThrow(
                        () -> new CustomException(ErrorCode.NEWS_CATEGORY_NOT_FOUND)
                );

        news.updateNews(newsCategory, requestDto.newsTitle(), requestDto.description());

        return NewsResponse.from(news);
    }

    @Transactional
    public void deleteNews(Long newsId, Long memberId) {
        News news = newsRepository.findById(newsId).orElseThrow(
                () -> new CustomException(ErrorCode.NEWS_NOT_FOUND)
        );

        if (!memberId.equals(news.getMember().getId())) {
            throw new CustomException(ErrorCode.NO_DELETE_PERMISSION);
        }

        newsRepository.delete(news);
    }
}
