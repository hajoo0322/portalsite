package com.portalSite.news.dto.response;

import java.util.List;

public record NewsListResponse(
        String message,
        List<NewsResponse> newsList
) {
    public static NewsListResponse from(String message, List<NewsResponse> newsList){
        return new NewsListResponse(message, newsList);
    }
}
