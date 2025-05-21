package com.portalSite.acquisition.service;

import com.portalSite.acquisition.dto.kafkaDto.AutocompleteSuggestionResponse;
import com.portalSite.acquisition.dto.kafkaDto.KeywordScore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsMetadata;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class PopularKeywordSyncScheduler implements InitializingBean {

    private final StreamsBuilderFactoryBean factoryBean;
    private final RedisTemplate<String, String> redisTemplate;
    private final WebClient.Builder webclientBuilder;

    private KafkaStreams kafkaStreams;

    @Override
    public void afterPropertiesSet() {
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            this.kafkaStreams = factoryBean.getKafkaStreams();
            if (this.kafkaStreams == null) {
                log.warn("❗ KafkaStreams is still null after delay.");
            } else {
                log.info("✅ KafkaStreams READY: {}", kafkaStreams.state());
            }
        }, 10, TimeUnit.SECONDS);
    }


    @Scheduled(fixedDelay = 10000)
    public void syncTopKeywordToRedis() {
        System.out.println("Current State: " + kafkaStreams.state());

        Collection<StreamsMetadata> metadataList = kafkaStreams.streamsMetadataForStore("keyword-score-store");
        PriorityQueue<KeywordScore> heap = new PriorityQueue<>(10, Comparator.comparingDouble(KeywordScore::score));
        List<Mono<List<KeywordScore>>> calls = new ArrayList<>();

        System.out.println("📭 metadataList.size(): " + metadataList.size());

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
                .filter(list -> !list.isEmpty())  // 빈 응답 제거
                .doOnNext(list -> {
                    System.out.println("📦 응답 리스트 크기: " + list.size());
                    for (KeywordScore ks : list) {
                        heap.offer(ks);
                        if (heap.size() > 10) heap.poll();
                    }
                })
                .doOnComplete(() -> {
                    List<KeywordScore> top10 = new ArrayList<>(heap);
                    top10.sort((a, b) -> Double.compare(b.score(), a.score()));

                    redisTemplate.delete("popular:keywords");
                    for (KeywordScore ks : top10) {
                        redisTemplate.opsForZSet().add("popular:keywords", ks.keyword(), ks.score());
                        System.out.println("✅ 저장됨! " + ks.keyword() + " - " + ks.score());
                    }
                })
                .doOnError(e -> System.err.println("🔥 Flux 오류: " + e.getMessage()))
                .subscribe();
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void syncAutocompleteToRedis() {
        Collection<StreamsMetadata> metadataList =
                kafkaStreams.streamsMetadataForStore("autocomplete-score-store");

        List<Mono<Map<String, List<AutocompleteSuggestionResponse>>>> calls = new ArrayList<>();

        for (StreamsMetadata metadata : metadataList) {
            String baseUrl = "http://" + metadata.host() + ":" + metadata.port();
            Mono<Map<String, List<AutocompleteSuggestionResponse>>> response = webclientBuilder.build()
                    .get()
                    .uri(baseUrl + "/top-autocomplete")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, List<AutocompleteSuggestionResponse>>>() {})
                    .onErrorResume(e -> {
                        System.err.println("요청 실패: " + baseUrl);
                        return Mono.empty();
                    });
            calls.add(response);
        }

        Flux.merge(calls)
                .doOnNext(responseMap -> {
                    for (Map.Entry<String, List<AutocompleteSuggestionResponse>> entry : responseMap.entrySet()) {
                        List<AutocompleteSuggestionResponse> suggestions = entry.getValue();

                        for (AutocompleteSuggestionResponse suggestion : suggestions) {
                            String full = suggestion.suggestion(); // ex) 사물인터넷
                            double score = suggestion.score();

                            for (int i = 1; i <= full.length(); i++) {
                                String prefix = full.substring(0, i); // 사, 사물, 사물인...

                                String redisKey = "autocomplete:" + prefix;
                                redisTemplate.opsForZSet().add(redisKey, full, score);
                            }
                        }
                    }
                })
                .subscribe();
    }
}
