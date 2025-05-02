package com.portalSite.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portalSite.acquisition.dto.kafkaDto.PopularSearchEvent;
import jdk.jfr.Event;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Bean
    public KStream<String, String> keywordStream(StreamsBuilder streamsBuilder) {
        KStream<String, String> stream = streamsBuilder.stream(
                "popular-search-events",
                Consumed.with(
                        Serdes.String(),
                        Serdes.String()
                )
        );
        KStream<String, PopularSearchEvent> events = stream.mapValues(this::parseToEvent);

        KStream<String, PopularSearchEvent> filtered = events.filter(
                (key, event) -> event.keyword() != null
        );
        filtered.groupByKey()
                .aggregate(
                        () -> 0.0,
                        (keyword, event, score) -> score + calculateScore(event),
                        Materialized.<String, Double, KeyValueStore<Bytes, byte[]>>as("keyword-score-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(Serdes.Double())
                )
                .toStream()
                .peek((key, value) -> System.out.println("[DEBUG] Aggregated: " + key + " -> " + value))
                .to("debug-output-topic", Produced.with(Serdes.String(), Serdes.Double()));

        return stream;
    }


    private PopularSearchEvent parseToEvent(String json) {
        try {
            return new ObjectMapper().readValue(json, PopularSearchEvent.class);
        } catch (JsonProcessingException e) {
            System.err.println("[ERROR] Failed to parse event: " + json);
            throw new RuntimeException(e);
        }
    }

    private Double calculateScore(PopularSearchEvent event) {
        return switch (event.eventType()) {
            case CLICK -> 3.0;
            case DWELL -> event.dwellTime() != null ? event.dwellTime() / 10.0 : 1.0;
            case INPUT -> 1.0;
            default -> 0.0;
        };
    }
}
