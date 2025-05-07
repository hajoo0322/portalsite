package com.portalSite.search.controller;

import com.portalSite.comment.entity.PostType;
import com.portalSite.search.dto.response.SearchResponse;
import com.portalSite.search.service.SearchService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResponse> search(
            @RequestParam("keyword")
            @NotBlank(message = "검색어를 입력해주세요")
            @Size(min = 1, message = "검색어는 1글자 이상 입력해주세요") String keyword,
            @RequestParam(value = "writer") String writer,
            @RequestParam(value = "created_at_start") LocalDateTime createdAtStart,
            @RequestParam(value = "created_at_end") LocalDateTime createdAtEnd,
            @RequestParam(value = "desc", defaultValue = "true") boolean desc,
            @RequestParam(value = "postType", required = false) PostType postType,
            @PageableDefault(sort = "id", direction = DESC) Pageable pageable
    ) {
        SearchResponse response = searchService.
                search(keyword, writer, createdAtStart, createdAtEnd, desc, postType, pageable);
        return ResponseEntity.ok(response);
    }
}
