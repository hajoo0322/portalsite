package com.portalSite.acquisition.dto.kafkaDto;

import com.portalSite.acquisition.EventType;
import com.portalSite.acquisition.dto.request.AutocompleteClickRequest;
import com.portalSite.acquisition.dto.request.SearchClickRequest;
import com.portalSite.acquisition.dto.request.SearchDwellRequest;

import java.time.LocalDateTime;

public record PopularSearchEvent(
        EventType eventType,
        String keyword,
        Boolean resultClicked,
        String clickedDocumentId,
        LocalDateTime timeStamp,
        Integer dwellTime,
        LocalDateTime exitedAt
) {
    public static PopularSearchEvent of(SearchClickRequest searchClickRequest, String clientIp) {
        return new PopularSearchEvent(
                searchClickRequest.eventType()
                ,searchClickRequest.keyword()
                ,searchClickRequest.resultClicked(),
                searchClickRequest.clickedDocumentId(),
                searchClickRequest.timeStamp(),
                null,
                null
        );
    }

    public static PopularSearchEvent of(SearchDwellRequest searchDwellRequest, String clientIp) {
        return new PopularSearchEvent(
                searchDwellRequest.eventType(),
                searchDwellRequest.keyword(),
                null,
                null,
                null,
                searchDwellRequest.dwellTime(),
                searchDwellRequest.exitedAt()
        );
    }
}
