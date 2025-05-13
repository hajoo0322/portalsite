package com.portalSite.common.infra.redis;

import com.portalSite.comment.entity.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final Duration DEFAULT_TTL = Duration.ofSeconds(10);

    public Object getSearchResult(String keyword, String writer,
                                  LocalDateTime createdAtStart, LocalDateTime createdAtEnd,
                                  PostType postType, int pageNo, int pageSize,
                                  Sort.Direction direction) {
        String key = makeSearchKey(keyword, writer, createdAtStart, createdAtEnd, postType, pageNo, pageSize, direction);
        return redisTemplate.opsForValue().get(key);
    }

    public void cacheSearchResult(String keyword, String writer,
                                  LocalDateTime createdAtStart, LocalDateTime createdAtEnd,
                                  PostType postType, int pageNo, int pageSize,
                                  Sort.Direction direction, Object value) {
        String key = makeSearchKey(keyword, writer, createdAtStart, createdAtEnd, postType, pageNo, pageSize, direction);
        redisTemplate.opsForValue().set(key, value, DEFAULT_TTL);
    }

    public void evictSearchResult(String keyword, String writer,
                                  LocalDateTime createdAtStart, LocalDateTime createdAtEnd,
                                  PostType postType, int pageNo, int pageSize,
                                  Sort.Direction direction) {
        String key = makeSearchKey(keyword, writer, createdAtStart, createdAtEnd, postType, pageNo, pageSize, direction);
        redisTemplate.delete(key);
    }

    public String makeSearchKey(String keyword, String writer,
                                LocalDateTime createdAtStart, LocalDateTime createdAtEnd,
                                PostType postType, int pageNo, int pageSize,
                                Sort.Direction direction) {

        return "search:all:" + "keyword=" + (keyword != null ? keyword : "null") +
                "|writer=" + (writer != null ? writer : "null") +
                "|start=" + (createdAtStart != null ? createdAtStart.toString() : "null") +
                "|end=" + (createdAtEnd != null ? createdAtEnd.toString() : "null") +
                "|type=" + (postType != null ? postType.name() : "null") +
                "|page=" + pageNo +
                "|size=" + pageSize +
                "|direction=" + (direction != null ? direction.name() : "null");
    }
}
