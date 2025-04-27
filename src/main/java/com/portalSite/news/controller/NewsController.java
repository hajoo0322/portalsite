package com.portalSite.news.controller;

import com.portalSite.news.dto.request.*;
import com.portalSite.news.dto.response.*;
import com.portalSite.news.service.NewsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

  private final NewsService newsService;

  /*TODO 기자만 작성 가능하도록 권한 추가*/
  @PostMapping()
  public ResponseEntity<NewsResponse> createNews(
      @Valid @RequestBody NewsCreateRequest requestDto
  ) {
    Long memberId = 0L; /*TODO JWT 구현 후 추가*/
    NewsResponse responseDto = NewsResponse.from(
        newsService.createNews(requestDto, memberId));

    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  @GetMapping("/{newsId}")
  public ResponseEntity<NewsResponse> getNewsById(@PathVariable Long newsId) {
    NewsResponse responseDto = NewsResponse.from(newsService.getNewsById(newsId));

    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  @GetMapping("/categories/{categoryId}")
  public ResponseEntity<List<NewsResponse>> getNewsListByCategory(
      @PathVariable Long categoryId
  ){
    List<NewsResponse> responseDtoList = newsService.getNewsListByCategory(categoryId).stream()
        .map(NewsResponse::from)
        .toList();

    return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
  }

  @PatchMapping("/{newsId}")
  public ResponseEntity<NewsResponse> updateNews(
    @PathVariable Long newsId,
    @RequestBody NewsRequest requestDto
  ) {
    Long memberId = 0L; /*TODO JWT 구현 후 추가*/
    NewsResponse responseDto = NewsResponse.from(newsService.updateNews(requestDto, newsId, memberId));

    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  @DeleteMapping("/{newsId}")
  public ResponseEntity<Null> deleteNews(@PathVariable Long newsId){
    Long memberId = 0L; /*TODO JWT 구현 후 추가*/
    newsService.deleteNews(newsId, memberId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}
