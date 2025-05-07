package com.portalSite.search.dto.response;

import com.portalSite.blog.dto.response.BlogPostResponse;
import com.portalSite.cafe.dto.CafePostResponse;
import com.portalSite.news.dto.response.NewsResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public record SearchResponse(
        List<BlogPostResponse> blogPostList,
        List<CafePostResponse> cafePostList,
        List<NewsResponse> newsList,
        int page,
        int size
) {
    public static SearchResponse from(List<BlogPostResponse> blogPosts, List<CafePostResponse> cafePosts, List<NewsResponse> newsResponses, Pageable pageable) {
        return new SearchResponse(blogPosts, cafePosts, newsResponses, pageable.getPageNumber(), pageable.getPageSize());
    }
}
