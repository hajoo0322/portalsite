package com.portalSite.acquisition.controller;

import com.portalSite.acquisition.service.PopularKeywordSyncScheduler;
import com.portalSite.acquisition.service.PopularKeywordSyncScheduler.KeywordScore;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

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
}
