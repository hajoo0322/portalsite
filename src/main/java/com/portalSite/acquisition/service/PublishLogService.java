package com.portalSite.acquisition.service;

import com.portalSite.acquisition.dto.kafkaDto.AutocompleteEvent;
import com.portalSite.acquisition.dto.kafkaDto.PopularSearchEvent;
import com.portalSite.acquisition.dto.request.AutocompleteClickRequest;
import com.portalSite.acquisition.dto.request.SearchClickRequest;
import com.portalSite.acquisition.dto.request.SearchDwellRequest;
import com.portalSite.common.infra.JsonHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublishLogService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonHelper jsonHelper;

    private static final String POPULAR_SEARCH_EVENTS = "popular-search-events";
    private static final String AUTOCOMPLETE_EVENTS = "Autocomplete-events";

    public void sendSearchClickEvent(SearchClickRequest searchClickRequest, String clientIp) {
        PopularSearchEvent popularSearchEvent = PopularSearchEvent.of(searchClickRequest, clientIp);
            String payload = jsonHelper.toJson(popularSearchEvent);
            kafkaTemplate.send(POPULAR_SEARCH_EVENTS, searchClickRequest.keyword(), payload);
    }

    public void sendAutocompleteClickEvent(AutocompleteClickRequest autocompleteClickRequest, String clientIp) {
        AutocompleteEvent autocompleteEvent = AutocompleteEvent.of(autocompleteClickRequest, clientIp);
        String payload = jsonHelper.toJson(autocompleteEvent);
        kafkaTemplate.send(AUTOCOMPLETE_EVENTS, autocompleteEvent.keyword(), payload);
    }

    public void sendDwellEvent(SearchDwellRequest searchDwellRequest, String clientIp) {
        PopularSearchEvent popularSearchEvent = PopularSearchEvent.of(searchDwellRequest, clientIp);
        String payload = jsonHelper.toJson(popularSearchEvent);
        kafkaTemplate.send(POPULAR_SEARCH_EVENTS, searchDwellRequest.keyword(), payload);
    }
}
