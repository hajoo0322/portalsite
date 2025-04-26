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
        news.getMember().getName(), /*TODO MemberEntity 참고하여 필드명 수정*/
        news.getNewsCategory().getId(),
        news.getNewsTitle(),
        news.getDescription(),
        news.getCreatedAt()
    );
  }
}
