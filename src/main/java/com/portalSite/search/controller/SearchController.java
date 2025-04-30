package com.portalSite.search.controller;

import com.portalSite.search.dto.request.SearchRequest;
import com.portalSite.search.dto.response.SearchResponse;
import com.portalSite.search.service.SearchService;
import jakarta.validation.Valid;
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
            @ModelAttribute @Valid SearchRequest request,
            @PageableDefault(size = 10, sort = "id", direction = DESC) Pageable pageable
            ) {
        SearchResponse response = searchService.search(request, pageable);
        return ResponseEntity.ok(response);
    }
}
