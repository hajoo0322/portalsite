package com.portalSite.search.controller;

import com.portalSite.comment.entity.PostType;
import com.portalSite.kafka.KeywordProducer;
import com.portalSite.search.dto.response.SearchResponse;
import com.portalSite.search.dto.response.TopKeywordsResponse;
import com.portalSite.search.service.SearchService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final KeywordProducer keywordProducer;
    private final RedisTemplate<String,String> redisTemplate;

    @GetMapping
    public ResponseEntity<SearchResponse> search(
            @RequestParam("keyword")
            @NotBlank(message = "검색어를 입력해주세요")
            @Size(min = 1, message = "검색어는 1글자 이상 입력해주세요") String keyword,
            @RequestParam(value = "postType", required = false) PostType postType,
            @PageableDefault(sort = "id", direction = DESC) Pageable pageable
    ) {
        keywordProducer.publishRawKeywordInputEvent(keyword); //kafka로 검색어 publish
        SearchResponse response = searchService.search(keyword, pageable, postType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v2")
    public ResponseEntity<SearchResponse> searchV2(
            @RequestParam("keyword")
            @NotBlank(message = "검색어를 입력해주세요")
            @Size(min = 1, message = "검색어는 1글자 이상 입력해주세요") String keyword,
            @RequestParam(value = "postType", required = false) PostType postType,
            @PageableDefault(sort = "id", direction = DESC) Pageable pageable
    ) {
        keywordProducer.publishRawKeywordInputEvent(keyword); //kafka로 검색어 publish
        SearchResponse response = searchService.searchV2(keyword, pageable, postType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-keywords")
    public ResponseEntity<List<TopKeywordsResponse>> getKeywordRanking(){
        //아래 linkedHashSet은 삽입 순서 유지
        Set<String> result = redisTemplate.opsForZSet().reverseRange("popular:keywords", 0, 9);

        if(result == null || result.isEmpty()) {
            throw new RuntimeException(); /*TODO notfound 예외처리*/
        }

        List<TopKeywordsResponse> responseList = new ArrayList<>();

        int rank = 1;
        for(String keyword : result) {
            responseList.add(new TopKeywordsResponse(rank++, keyword));
        }

        return ResponseEntity.ok(responseList);
    }
}
