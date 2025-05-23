package com.portalSite.news.dto.response;

import com.portalSite.news.entity.NewsCategory;

public record NewsCategoryResponse(
    Long categoryId,
    String categoryName,
    Long parentId,
    String parentName
) {

  public static NewsCategoryResponse from(NewsCategory newsCategory) {
    return new NewsCategoryResponse(
            newsCategory.getId(),
            newsCategory.getName(),
            newsCategory.getParent() != null ? newsCategory.getParent().getId() : null,
            newsCategory.getParent() != null ? newsCategory.getParent().getName() : null
    );
  }
}
