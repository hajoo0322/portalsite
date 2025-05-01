package com.portalSite.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchRankingService {

    private final StringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String RANKING_KEY_PREFIX = "search_rank:current";
    private static final String TOPIC = "search-log";


    public void sendSearchLog(String keyword) {
        kafkaTemplate.send(TOPIC, keyword);
    }

    public List<String> getTopSearchKeywords(int topN) {
        Set<String> result = redisTemplate.opsForZSet()
            .reverseRange(RANKING_KEY_PREFIX, 0, topN - 1);

        if (result == null) return List.of();
        return new ArrayList<>(result);
    }

    @KafkaListener(topics = "search-log", groupId = "search-log-group")
    public void consume(String keyword) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.incrementScore(RANKING_KEY_PREFIX, keyword, 1.0);
    }

    @Scheduled(fixedRate = 60_000)
    public void decaySearchScores() {
        String key = RANKING_KEY_PREFIX;
        double decayFactor = 0.9;

        Set<ZSetOperations.TypedTuple<String>> tuples =
            redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);

        if (tuples == null) return;

        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            String keyword = tuple.getValue();
            Double score = tuple.getScore();
            if (keyword == null || score == null) continue;

            double newScore = score * decayFactor;

            if (newScore < 0.01) {
                redisTemplate.opsForZSet().remove(key, keyword);
            } else {
                redisTemplate.opsForZSet().add(key, keyword, newScore);
            }
        }

        redisTemplate.opsForZSet().removeRange(key, 1000, -1);
    }


}
