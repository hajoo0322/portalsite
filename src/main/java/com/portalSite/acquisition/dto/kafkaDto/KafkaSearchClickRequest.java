package com.portalSite.acquisition.dto.kafkaDto;

import com.portalSite.acquisition.EventType;
import com.portalSite.acquisition.dto.request.SearchClickRequest;

import java.time.LocalDateTime;

public record KafkaSearchClickRequest(
        String clientIp,
        EventType eventType,
        String keyword,
        Boolean resultClicked,
        String clickedDocumentId,
        LocalDateTime timeStamp
) {
    public static KafkaSearchClickRequest of(SearchClickRequest searchClickRequest, String clientIp) {
        return new KafkaSearchClickRequest(
                clientIp,
                searchClickRequest.eventType(),
                searchClickRequest.keyword(),
                searchClickRequest.resultClicked(),
                searchClickRequest.clickedDocumentId(),
                searchClickRequest.timeStamp()
        );
    }
}
