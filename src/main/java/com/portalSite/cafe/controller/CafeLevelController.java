package com.portalSite.cafe.controller;

import com.portalSite.cafe.dto.CafeLevelRequest;
import com.portalSite.cafe.dto.CafeLevelRequestList;
import com.portalSite.cafe.dto.CafeLevelResponse;
import com.portalSite.cafe.service.CafeLevelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cafes/{cafeId}/cafe-levels")
@RequiredArgsConstructor
public class CafeLevelController {

    private final CafeLevelService cafeLevelService;

    @PostMapping
    public ResponseEntity<List<CafeLevelResponse>> addCafeLevel(@RequestBody @Valid CafeLevelRequestList cafeLevelRequestList,@PathVariable Long cafeId) {
        List<CafeLevelResponse> cafeLevelResponses = cafeLevelService.addCafeLevel(cafeLevelRequestList, cafeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cafeLevelResponses);
    }

    @GetMapping
    public ResponseEntity<List<CafeLevelResponse>> getCafeLevel(@PathVariable Long cafeId) {
        List<CafeLevelResponse> cafeLevel = cafeLevelService.getCafeLevel(cafeId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeLevel);
    }

    @PatchMapping
    public ResponseEntity<List<CafeLevelResponse>> updateCafeLevel(@RequestBody @Valid CafeLevelRequestList cafeLevelRequestList) {
        List<CafeLevelResponse> cafeLevel = cafeLevelService.updateCafeLevel(cafeLevelRequestList);
        return ResponseEntity.status(HttpStatus.OK).body(cafeLevel);
    }
}
