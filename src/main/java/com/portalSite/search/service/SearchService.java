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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final BlogPostRepository blogPostRepository;
    private final CafePostRepository cafePostRepository;
    private final NewsRepository newsRepository;

    public SearchResponse search(SearchRequest request) {
        List<BlogPostResponse> blogPostList = blogPostRepository.findAllByKeyword(request.keyWord()).stream()
                .map(BlogPostResponse::from).toList();
        List<CafePostResponse> cafePostList = cafePostRepository.findAllByKeyword(request.keyWord()).stream()
                .map(CafePostResponse::from).toList();
        List<NewsResponse> newsList = newsRepository.findAllByKeyword(request.keyWord()).stream()
                .map(NewsResponse::from).toList();

        return SearchResponse.from(blogPostList, cafePostList, newsList);
    }
}
