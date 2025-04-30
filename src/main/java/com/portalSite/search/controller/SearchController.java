package com.portalSite.search.controller;

import com.portalSite.search.dto.request.SearchRequest;
import com.portalSite.search.dto.response.SearchResponse;
import com.portalSite.search.service.SearchService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResponse> search(
            @RequestParam("keyword")
            @NotBlank(message="검색어를 입력해주세요")
            @Size(min = 1, message = "검색어는 1글자 이상 입력해주세요")String keyword,
            @PageableDefault(sort = "id", direction = DESC) Pageable pageable
            ) {
        SearchResponse response = searchService.search(keyword, pageable);
        return ResponseEntity.ok(response);
    }
}
