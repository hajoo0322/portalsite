package com.portalSite.kafka;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/searchtest")
public class TestKafkaController {

    private final SearchRankingService searchRankingService;


    @GetMapping("/ranking")
    public List<String> getSearchRanking(@RequestParam(defaultValue = "10") int size) {
        return searchRankingService.getTopSearchKeywords(size);
    }

    @PostMapping("/send")
    public String send(@RequestParam String keyword) {
        searchRankingService.sendSearchLog(keyword);
        return "sent: " + keyword;
    }


}