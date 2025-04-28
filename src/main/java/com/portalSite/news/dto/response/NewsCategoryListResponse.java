package com.portalSite.news.dto.response;

import java.util.List;

public record NewsCategoryListResponse(
        String message,
        List<NewsCategoryResponse> newsList
) {
    public static NewsCategoryListResponse from(String message, List<NewsCategoryResponse> newsList){
        return new NewsCategoryListResponse(message, newsList);
    }
}
