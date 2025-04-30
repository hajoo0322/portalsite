package com.portalSite.common.config;

import org.springframework.context.annotation.Bean;

public class KafkaTopicConfig {

    @Bean
    public NewTopic searchLogTopic() {
        return TopicBuilder.name("search-log")
            .partitions(3)
            .replicas(1)
            .build();
    }


}
