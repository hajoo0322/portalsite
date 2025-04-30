package com.portalSite.search.service;

import com.portalSite.blog.dto.response.BlogPostResponse;
import com.portalSite.blog.repository.BlogPostRepository;
import com.portalSite.cafe.dto.CafePostResponse;
import com.portalSite.cafe.repository.CafePostRepository;
import com.portalSite.news.dto.response.NewsResponse;
import com.portalSite.news.repository.NewsRepository;
import com.portalSite.search.dto.request.SearchRequest;
import com.portalSite.search.dto.response.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final BlogPostRepository blogPostRepository;
    private final CafePostRepository cafePostRepository;
    private final NewsRepository newsRepository;

    public SearchResponse search(SearchRequest request, Pageable pageable) {
        String keyword = request.keyword();

        List<BlogPostResponse> blogPostList = blogPostRepository.findAllByKeyword(keyword, pageable).stream()
                .map(BlogPostResponse::from).toList();
        List<CafePostResponse> cafePostList = cafePostRepository.findAllByKeyword(keyword, pageable).stream()
                .map(CafePostResponse::from).toList();
        List<NewsResponse> newsList = newsRepository.findAllByKeyword(keyword, pageable).stream()
                .map(NewsResponse::from).toList();

        return SearchResponse.from(blogPostList, cafePostList, newsList);
    }
}
