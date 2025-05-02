package com.portalSite.acquisition.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portalSite.acquisition.dto.kafkaDto.KafkaSearchClickRequest;
import com.portalSite.acquisition.dto.request.SearchClickRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublishLogService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "popular-search-events";

    public void sendClickEvent(SearchClickRequest searchClickRequest, String clientIp) {
        KafkaSearchClickRequest kafkaSearchClickRequest = KafkaSearchClickRequest.of(searchClickRequest, clientIp);
        try {
            String payload = objectMapper.writeValueAsString(kafkaSearchClickRequest);
            kafkaTemplate.send(TOPIC, searchClickRequest.keyword(), payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
