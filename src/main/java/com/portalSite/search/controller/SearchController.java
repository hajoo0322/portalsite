package com.portalSite.search.controller;

import com.portalSite.search.dto.request.SearchRequest;
import com.portalSite.search.dto.response.SearchResponse;
import com.portalSite.search.service.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResponse> search(
            @ModelAttribute @Valid SearchRequest request) {
        SearchResponse response = searchService.search(request);
        return ResponseEntity.ok(response);
    }
}
