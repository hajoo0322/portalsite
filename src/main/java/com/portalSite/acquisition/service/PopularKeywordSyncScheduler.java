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

@Component
@RequiredArgsConstructor
public class PopularKeywordSyncScheduler implements InitializingBean {

    private final StreamsBuilderFactoryBean factoryBean;
    private final RedisTemplate<String, String> redisTemplate;
    private final WebClient.Builder webclientBuilder;

    private KafkaStreams kafkaStreams;

    @Override
    public void afterPropertiesSet() {
        this.kafkaStreams = waitForKafkaStreams(factoryBean);
    }

    private KafkaStreams waitForKafkaStreams(StreamsBuilderFactoryBean factoryBean) {
        return factoryBean.getKafkaStreams();
    }
    @Scheduled(fixedDelay = 10000)
    public void syncTopKeywordToRedis() {

        Collection<StreamsMetadata> metadataList = kafkaStreams.streamsMetadataForStore("keyword-score-store");
        PriorityQueue<KeywordScore> heap = new PriorityQueue<>(10, Comparator.comparingDouble(KeywordScore::score));
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
                    top10.sort((a, b) -> Double.compare(b.score(), a.score()));

                    redisTemplate.delete("popular:keywords");
                    for (KeywordScore ks : top10) {
                        redisTemplate.opsForZSet().add("popular:keywords", ks.keyword(), ks.score());
                    }
                })
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
                        String keyword = entry.getKey();
                        List<AutocompleteSuggestionResponse> suggestions = entry.getValue();

                        redisTemplate.delete("autocomplete:" + keyword);
                        for (AutocompleteSuggestionResponse suggestion : suggestions) {
                            redisTemplate.opsForZSet().add("autocomplete:" + keyword, suggestion.suggestion(), suggestion.score());
                        }
                    }
                })
                .subscribe();
    }
}
