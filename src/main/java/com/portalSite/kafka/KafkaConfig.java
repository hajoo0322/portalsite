package com.portalSite.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic searchLogTopic() {
        return TopicBuilder.name("search-log")
            .partitions(3)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic autocompleteTopic() {
        return TopicBuilder.name("Autocomplete-events")
                .partitions(3)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic popularSearchTopic() {
        return TopicBuilder.name("popular-search-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        return new KafkaStreamsInteractiveQueryService(streamsBuilderFactoryBean);
    }
}
