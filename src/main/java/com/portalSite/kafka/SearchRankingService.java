package com.portalSite.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchRankingService {

    private final StringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String RANKING_KEY_PREFIX = "search_rank:";
    private static final String TOPIC = "search-log";

    private static final int RANKTIME = 5;


    public void sendSearchLog(String keyword) {
        kafkaTemplate.send(TOPIC, keyword);
    }

    public List<String> getTopSearchKeywords(int topN) {

        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        Map<String, Double> scoreMap = new HashMap<>();

        for (int i = 0; i < RANKTIME; i++) {
            String key = getMinuteKey(LocalDateTime.now().minusMinutes(i));
            Set<ZSetOperations.TypedTuple<String>> tuples = zSetOps.reverseRangeWithScores(key, 0,
                -1);

            if (tuples == null) {
                continue;
            }

            for (ZSetOperations.TypedTuple<String> tuple : tuples) {
                scoreMap.merge(tuple.getValue(), tuple.getScore(), Double::sum);
            }
        }

        List<String> result =scoreMap.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(topN)
            .map(Map.Entry::getKey)
            .toList();;

        return result;
    }

    @KafkaListener(topics = "search-log", groupId = "search-log-group")
    public void consume(String keyword) {

        String key = getMinuteKey(LocalDateTime.now());
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();

        zSetOps.incrementScore(key, keyword, 1.0);
        redisTemplate.expire(key, java.time.Duration.ofMinutes(RANKTIME)); // TTL 설정 (5분)
    }

    private String getMinuteKey(LocalDateTime time) {

        return RANKING_KEY_PREFIX + time.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
    }
}
