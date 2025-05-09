package com.portalSite.search.service;

import com.portalSite.blog.dto.response.BlogPostResponse;
import com.portalSite.blog.repository.BlogPostRepository;
import com.portalSite.cafe.dto.CafePostResponse;
import com.portalSite.cafe.repository.CafePostRepository;
import com.portalSite.comment.entity.PostType;
import com.portalSite.news.dto.response.NewsResponse;
import com.portalSite.news.repository.NewsRepository;
import com.portalSite.search.dto.response.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final BlogPostRepository blogPostRepository;
    private final CafePostRepository cafePostRepository;
    private final NewsRepository newsRepository;

    /**
     * 검색 기준<br>
     * 카페, 뉴스 : 제목, 내용<br>
     * 블로그 : 제목, 내용
     */
    public SearchResponse searchV3(
            String keyword, String writer, LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd, PostType postType, Pageable pageable) {

        List<BlogPostResponse> blogPostList = postType == null || postType == PostType.BLOG
                ? blogPostRepository.findAllByKeywordV2(
                        keyword, writer, createdAtStart, createdAtEnd, pageable).getContent()
                : null;

        List<CafePostResponse> cafePostList = postType == null || postType == PostType.CAFE
                ? cafePostRepository.findAllByKeywordV2(
                        keyword, writer, createdAtStart, createdAtEnd, pageable).getContent()
                : null;

        List<NewsResponse> newsList = postType == null || postType == PostType.NEWS
                ? newsRepository.findAllByKeywordV2(
                        keyword, writer, createdAtStart, createdAtEnd, pageable).getContent()
                : null;

        return SearchResponse.from(blogPostList, cafePostList, newsList, pageable);
    }

    public SearchResponse searchV2(String keyword, Pageable pageable, PostType postType) {

        List<BlogPostResponse> blogPostList = postType == null || postType == PostType.BLOG ?
                blogPostRepository.findAllByKeywordWithIndex(keyword, pageable).stream().toList() : null;

        List<CafePostResponse> cafePostList = postType == null || postType == PostType.CAFE ?
                cafePostRepository.findAllByKeywordWithIndex(keyword, pageable).stream().toList() : null;

        List<NewsResponse> newsList = postType == null || postType == PostType.NEWS ?
                newsRepository.findAllByKeywordWithIndex(keyword, pageable).stream().toList() : null;

        return SearchResponse.from(blogPostList, cafePostList, newsList, pageable);
    }
}
