package com.portalSite.cafe.controller;

import com.portalSite.cafe.dto.*;
import com.portalSite.cafe.service.CafeMemberService;
import com.portalSite.cafe.service.CafeService;
import com.portalSite.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cafes")
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;


    @PostMapping
    public ResponseEntity<CafeResponse> addCafe(
            @RequestBody @Valid CafeRequest cafeRequest) {
        CafeResponse cafeResponse = cafeService.addCafe(cafeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(cafeResponse);
    }

    @GetMapping("/duplication")
    public ResponseEntity<Void> duplicateCafeName(@RequestParam String cafeName) {
        cafeService.duplicateCafeName(cafeName);
        return ResponseEntity.status(HttpStatus.OK).build();
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
