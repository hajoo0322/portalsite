package com.portalSite.cafe.controller;

import com.portalSite.cafe.dto.CafeBoardRequest;
import com.portalSite.cafe.dto.CafeBoardResponse;
import com.portalSite.cafe.service.CafeBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cafes/{cafeId}/cafe-board")
@RequiredArgsConstructor
public class CafeBoardController {

    private final CafeBoardService cafeBoardService;

    @PostMapping
    public ResponseEntity<CafeBoardResponse> addCafeBoard(@RequestBody @Valid CafeBoardRequest cafeBoardRequest, @PathVariable Long cafeId) {
        CafeBoardResponse cafeBoardResponse = cafeBoardService.addCafeBoard(cafeBoardRequest, cafeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cafeBoardResponse);
    }

    @GetMapping
    public ResponseEntity<List<CafeBoardResponse>> getAllCafeBoard(@PathVariable Long cafeId) {
        List<CafeBoardResponse> cafeBoardList = cafeBoardService.getAllCafeBoard(cafeId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeBoardList);
    }

    @PatchMapping("/{cafeBoardId}")
    public ResponseEntity<CafeBoardResponse> updateCafeBoard(@RequestBody @Valid CafeBoardRequest cafeBoardRequest, @PathVariable Long cafeBoardId) {
        CafeBoardResponse cafeBoardResponse = cafeBoardService.updateCafeBoard(cafeBoardRequest, cafeBoardId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeBoardResponse);
    }

    @DeleteMapping("/{cafeBoardId}")
    public ResponseEntity<Void> deleteCafeBoard(@PathVariable Long cafeBoardId) {
        cafeBoardService.deleteCafeBoard(cafeBoardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
