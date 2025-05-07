package com.portalSite.news.dto.response;

import com.portalSite.news.entity.News;
import java.time.LocalDateTime;

public record NewsResponse(
    Long newsId,
    String name,
    Long categoryId,
    String newsTitle,
    String description,
    LocalDateTime createdAt) {

  public static NewsResponse from(News news){
    return new NewsResponse(
        news.getId(),
        news.getMember().getName(),
        news.getNewsCategory().getId(),
        news.getNewsTitle(),
        news.getDescription(),
        news.getCreatedAt()
    );
  }


}
