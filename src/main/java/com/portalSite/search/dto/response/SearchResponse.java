package com.portalSite.search.dto.response;

import com.portalSite.blog.dto.response.BlogPostResponse;
import com.portalSite.cafe.dto.CafePostResponse;
import com.portalSite.news.dto.response.NewsResponse;

import java.util.List;

public record SearchResponse(
        List<BlogPostResponse> blogPostList,
        List<CafePostResponse> cafePostList,
        List<NewsResponse> newsList
) {
    public static SearchResponse from(List<BlogPostResponse> blogPosts, List<CafePostResponse> cafePosts, List<NewsResponse> newsResponses) {
        return new SearchResponse(blogPosts, cafePosts, newsResponses);
    }
}
