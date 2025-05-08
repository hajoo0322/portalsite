package com.portalSite.acquisition.controller;

import com.portalSite.acquisition.dto.request.AutocompleteClickRequest;
import com.portalSite.acquisition.service.PublishLogService;
import com.portalSite.common.annotation.CurrentClientIp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AutocompleteController {

    private final PublishLogService publishLogService;

    @PostMapping
    public ResponseEntity<Void> logAutocompleteClick(
            @RequestBody AutocompleteClickRequest autocompleteClickRequest,
            @CurrentClientIp String clientIp
    ) {

        publishLogService.sendAutocompleteClickEvent(autocompleteClickRequest, clientIp);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
