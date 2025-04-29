package com.portalSite.news.controller;

import com.portalSite.member.entity.MemberRole;
import com.portalSite.news.dto.request.*;
import com.portalSite.news.dto.response.*;
import com.portalSite.news.service.NewsService;
import com.portalSite.security.AuthUser;
import jakarta.validation.Valid;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping()
    @PreAuthorize("hasRole('REPORTER')")
    public ResponseEntity<NewsResponse> createNews(
            @Valid @RequestBody NewsCreateRequest requestDto,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Long memberId = authUser.memberId();
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
    ) {
        List<NewsResponse> responseDtoList = newsService.getNewsListByCategory(categoryId).stream()
                .map(NewsResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @PatchMapping("/{newsId}")
    @Secured(MemberRole.Authority.REPORTER)
    public ResponseEntity<NewsResponse> updateNews(
            @PathVariable Long newsId,
            @RequestBody NewsRequest requestDto,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Long memberId = authUser.memberId();
        NewsResponse responseDto = NewsResponse.from(newsService.updateNews(requestDto, newsId, memberId));

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{newsId}")
    @Secured({MemberRole.Authority.REPORTER, MemberRole.Authority.ADMIN})
    public ResponseEntity<Void> deleteNews(
            @PathVariable Long newsId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Long memberId = authUser.memberId();
        newsService.deleteNews(newsId, memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
