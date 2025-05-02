package com.portalSite.acquisition.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsMetadata;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
@RequiredArgsConstructor
public class PopularKeywordSyncScheduler implements InitializingBean {

    private final StreamsBuilderFactoryBean factoryBean;
    private final RedisTemplate<String, String> redisTemplate;
    private final WebClient.Builder webclientBuilder;

    private  KafkaStreams kafkaStreams;

    @Override
    public void afterPropertiesSet() {
        this.kafkaStreams = waitForKafkaStreams(factoryBean);
    }

    private KafkaStreams waitForKafkaStreams(StreamsBuilderFactoryBean factoryBean) {
        while (factoryBean.getKafkaStreams() == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }
        return factoryBean.getKafkaStreams();
    }
    @Scheduled(fixedDelay = 10000)
    public void syncTopKeywordToRedis() {

        Collection<StreamsMetadata> metadataList = kafkaStreams.streamsMetadataForStore("keyword-score-store");
        PriorityQueue<KeywordScore> heap = new PriorityQueue<>(10, Comparator.comparingLong(KeywordScore::score));
        List<Mono<List<KeywordScore>>> calls = new ArrayList<>();

        for (StreamsMetadata metadata : metadataList) {
            String baseUrl = "http://" + metadata.host() + ":" + metadata.port();
            Mono<List<KeywordScore>> response = webclientBuilder.build()
                    .get()
                    .uri(baseUrl + "/top-keywords")
                    .retrieve()
                    .bodyToFlux(KeywordScore.class)
                    .collectList()
                    .onErrorResume(e -> {
                        System.err.println("요청 실패: " + baseUrl);
                        return Mono.empty();
                    });
            calls.add(response);
        }
        Flux.merge(calls)
                .doOnNext(list -> {
                    for (KeywordScore ks : list) {
                        heap.offer(ks);
                        if (heap.size() > 10) heap.poll();
                    }
                })
                .doOnComplete(() -> {
                    List<KeywordScore> top10 = new ArrayList<>(heap);
                    top10.sort((a, b) -> Long.compare(b.score(), a.score()));

                    redisTemplate.delete("popular:keywords");
                    for (KeywordScore ks : top10) {
                        redisTemplate.opsForZSet().add("popular:keywords", ks.keyword(), ks.score());
                    }
                })
                .subscribe();
    }

    public record KeywordScore(String keyword, long score) {}
}
