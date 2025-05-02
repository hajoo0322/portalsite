package com.portalSite.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.portalSite.acquisition.EventType;
import com.portalSite.acquisition.dto.kafkaDto.PopularSearchEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordProducer {

    private final KafkaTemplate<String, String> kafkaTemplate; //kafka에 메시지를 보낼 때 사용하는 템플릿 객체
    private static final String TOPIC = "popular-search-events"; // 메세지 전송 대상(topic)의 이름
    private final ObjectMapper objectMapper;

    public void publishRawKeywordInputEvent(String keyword){
        PopularSearchEvent event = new PopularSearchEvent(
                EventType.INPUT,
                keyword,
                false,
                null,
                LocalDateTime.now(),
                null,
                null
        );

        try{
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(TOPIC, keyword, json);
        }catch (JsonProcessingException e){
            log.error("[ERROR] kafka 메시지 직렬화 실패: {}", e.getMessage());
        }
    }
}
