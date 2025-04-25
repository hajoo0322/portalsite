package com.portalSite.cafe.controller;

import com.portalSite.cafe.dto.CafeRequest;
import com.portalSite.cafe.dto.CafeResponse;
import com.portalSite.cafe.service.CafeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cafes")
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;

    @PostMapping
    public ResponseEntity<CafeResponse> addCafe(@RequestBody @Valid CafeRequest requestCafe) {
        CafeResponse cafeResponse = cafeService.addCafe(requestCafe);
        return ResponseEntity.status(HttpStatus.CREATED).body(cafeResponse);
    }

    @GetMapping("/{cafeId}")
    public ResponseEntity<CafeResponse> getCafe(@PathVariable Long cafeId) {
        CafeResponse cafeResponse = cafeService.getCafe(cafeId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeResponse);
    }

    @PatchMapping("/{cafeId}")
    public ResponseEntity<CafeResponse> updateCafe(@RequestBody @Valid CafeRequest requestCafe, @PathVariable Long cafeId) {
        CafeResponse cafeResponse = cafeService.updateCafe(requestCafe, cafeId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeResponse);
    }

    @DeleteMapping("/{cafeId}")
    public ResponseEntity<Void> deleteCafe(@PathVariable Long cafeId) {
        cafeService.deleteCafe(cafeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
