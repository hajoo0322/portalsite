package com.portalSite.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portalSite.acquisition.dto.kafkaDto.AutocompleteEvent;
import com.portalSite.acquisition.dto.kafkaDto.PopularSearchEvent;
import com.portalSite.common.infra.JsonHelper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.time.LocalDateTime;

@Configuration
@EnableKafkaStreams
@RequiredArgsConstructor
public class KafkaStreamsConfig {

    private final JsonHelper jsonHelper;

    @Bean
    public KStream<String, String> popularKeywordsStream(StreamsBuilder streamsBuilder) {
        KStream<String, String> stream = streamsBuilder.stream(
                "popular-search-events",
                Consumed.with(
                        Serdes.String(),
                        Serdes.String()
                )
        );
        KStream<String, PopularSearchEvent> events = stream.mapValues(
                json -> jsonHelper.fromJson(json, PopularSearchEvent.class)
        );

        KStream<String, PopularSearchEvent> filtered = events.filter(
                (key, event) -> event.keyword() != null
        );
        filtered.groupByKey()
                .aggregate(
                        () -> 0.0,
                        (keyword, event, score) -> score + calculatePopularSearchScore(event),
                        Materialized.<String, Double, KeyValueStore<Bytes, byte[]>>as("keyword-score-store") //내가 뭘시발건드렸을까
                                .withKeySerde(Serdes.String())
                                .withValueSerde(Serdes.Double())
                )
                .toStream()
                .peek((key, value) -> System.out.println("[DEBUG] Aggregated: " + key + " -> " + value))
                .to("debug-output-topic", Produced.with(Serdes.String(), Serdes.Double()));

        return stream;
    }

    @Bean
    public KStream<String, String> autocompleteStream(StreamsBuilder streamsBuilder) {
        KStream<String, String> stream = streamsBuilder.stream("Autocomplete-events", Consumed.with(Serdes.String(), Serdes.String()));
        KStream<String, AutocompleteEvent> events = stream.mapValues(
                json -> jsonHelper.fromJson(json, AutocompleteEvent.class)
        );

        KStream<String, AutocompleteEvent> validEvents = events.filter(
                (key, event) ->
                        event.keyword() != null &&
                                !event.keyword().isEmpty() &&
                                event.suggestion() != null &&
                                !event.suggestion().isEmpty()
        );

        KStream<String, AutocompleteEvent> keyedByComposite = validEvents.selectKey(
                (key, event) -> event.keyword() + "::" + event.suggestion()
        );

        keyedByComposite
                .groupByKey(Grouped.with(Serdes.String(), new JsonSerde<>(AutocompleteEvent.class)))
                .aggregate(
                        () -> 0.0,
                        (compositeKey, event, score) -> score + calculateAutocompleteScore(event),
                        Materialized.<String, Double, KeyValueStore<Bytes, byte[]>>as("autocomplete-score-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(Serdes.Double())
                )
                .toStream()
                .peek((key, value) -> System.out.println("[AUTOCOMPLETE DEBUG] " + key + " -> " + value))
                .to("autocomplete-debug-topic", Produced.with(Serdes.String(), Serdes.Double()));
        return stream;
    }

    private Double calculateAutocompleteScore(AutocompleteEvent event) {
        return switch (event.eventType()) {
            case CLICK -> autocompleteClickScore(event);

            default -> 0.0;
        };
    }

    private Double autocompleteClickScore(AutocompleteEvent event) {
        double baseScore = 1.0;

        double positionScore = switch (event.position()) {
            case 0 -> 1.0;
            case 1 -> 0.8;
            case 2 -> 0.6;
            case 3 -> 0.4;
            default -> 0.2;
        };
        double similarityScore = event.suggestion().contains(event.keyword()) ? 1.0 : 0.5;
        return baseScore * positionScore * similarityScore;
    }


    private Double calculatePopularSearchScore(PopularSearchEvent event) {
        return switch (event.eventType()) {
            case CLICK -> popularSearchClickScore(event);
            case DWELL -> popularSearchDwellScore(event);
            case INPUT -> 1.0;

            default -> 0.0;
        };
    }

    private PopularSearchEvent parseToEvent(String json) {
        try {
            return new ObjectMapper().readValue(json, PopularSearchEvent.class);
        } catch (JsonProcessingException e) {
            System.err.println("[ERROR] Failed to parse event: " + json);
            throw new RuntimeException(e);
        }
    }

    private Double popularSearchDwellScore(PopularSearchEvent event) {
        int dwell = event.dwellTime() != null ? event.dwellTime() : 0;

        if (dwell <= 2) return -1.0;
        if (dwell <= 5) return 0.2;
        if (dwell <= 10) return 0.6;
        if (dwell <= 30) return 1.0;
        if (dwell <= 60) return 1.5;
        return 2.0;
    }

    private Double popularSearchClickScore(PopularSearchEvent event) {
            double baseScore = 0;

            if (Boolean.TRUE.equals(event.resultClicked())) {
                baseScore += 1.0;
            } else {
                baseScore -= 0.5;
            }

            if (event.clickedDocumentId() != null && !event.clickedDocumentId().isBlank()) {
                baseScore += 0.5;
            }

            long hoursAgo = Duration.between(event.timeStamp(), LocalDateTime.now()).toHours();
            double timeDecay = Math.max(0.1, 1.0 - (hoursAgo / 24.0)); // 1일 지나면 감소
            baseScore *= timeDecay;

            return baseScore;

    }
}
