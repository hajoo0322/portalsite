package com.portalSite.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeywordProducer {

    private final KafkaTemplate<String, String> kafkaTemplate; //kafka에 메시지를 보낼 때 사용하는 템플릿 객체
    private static final String TOPIC = "popular-search-events"; // 메세지 전송 대상(topic)의 이름

    public void publishRawKeyword(String keyword){
        kafkaTemplate.send(TOPIC, keyword, null);
    }
}
