package com.portalSite.acquisition.controller;

import com.portalSite.acquisition.dto.kafkaDto.AutocompleteSuggestionResponse;
import com.portalSite.acquisition.dto.kafkaDto.KeywordScore;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class KeywordQueryController {
    private final KafkaStreamsInteractiveQueryService queryService;


    @GetMapping("/top-keywords")
    public List<KeywordScore> topKeyword() {
        ReadOnlyKeyValueStore<String, Long> store = queryService.retrieveQueryableStore("keyword-score-store", QueryableStoreTypes.keyValueStore());
        PriorityQueue<KeywordScore> heap = new PriorityQueue<>(10, Comparator.comparingLong(KeywordScore::score));

        try (KeyValueIterator<String, Long> all = store.all()) {
            while (all.hasNext()) {
                var entry = all.next();
                heap.offer(new KeywordScore(entry.key, entry.value));
                if (heap.size() > 10) heap.poll();
            }
        }

        List<KeywordScore> top10 = new ArrayList<>(heap);
        top10.sort((a, b) -> Long.compare(b.score(), a.score()));
        return top10;
    }

    @GetMapping("/top-autocomplete")
    public Map<String, List<AutocompleteSuggestionResponse>> allAutocompleteSuggestions() {
        ReadOnlyKeyValueStore<String, Double> store = queryService
                .retrieveQueryableStore("autocomplete-score-store", QueryableStoreTypes.keyValueStore());

        Map<String, PriorityQueue<AutocompleteSuggestionResponse>> grouped = new HashMap<>();

        try (KeyValueIterator<String, Double> all = store.all()) {
            while (all.hasNext()) {
                KeyValue<String, Double> entry = all.next();
                String[] parts = entry.key.split("::");
                if (parts.length != 2) continue;

                String keyword = parts[0];
                String suggestion = parts[1];
                double score = entry.value;

                grouped.putIfAbsent(keyword, new PriorityQueue<>(Comparator.comparingDouble(AutocompleteSuggestionResponse::score)));
                grouped.get(keyword).offer(new AutocompleteSuggestionResponse(suggestion, score));

                if (grouped.get(keyword).size() > 5) grouped.get(keyword).poll();
            }
        }

        Map<String, List<AutocompleteSuggestionResponse>> result = new HashMap<>();
        for (Map.Entry<String, PriorityQueue<AutocompleteSuggestionResponse>> entry : grouped.entrySet()) {
            List<AutocompleteSuggestionResponse> list = new ArrayList<>(entry.getValue());
            list.sort((a, b) -> Double.compare(b.score(), a.score()));
            result.put(entry.getKey(), list);
        }

        return result;
    }
}
