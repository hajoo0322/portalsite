package com.portalSite.acquisition.controller;

import com.portalSite.acquisition.dto.SearchClickRequest;
import com.portalSite.acquisition.dto.SearchDwellRequest;
import com.portalSite.acquisition.service.PublishLogService;
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
public class SearchController {

    private final PublishLogService publishLogService;

    @PostMapping("/click")
    public ResponseEntity<Void> logClick(@RequestBody SearchClickRequest searchClickRequest) {

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/dwell")
    public ResponseEntity<Void> logDwell(@RequestBody SearchDwellRequest searchDwellRequest) {

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
