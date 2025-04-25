package com.portalSite.cafe.service;

import com.portalSite.cafe.dto.CafeResponse;
import com.portalSite.cafe.dto.CafeRequest;
import com.portalSite.cafe.entity.Cafe;
import com.portalSite.cafe.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CafeService{

    private final CafeRepository cafeRepository;

    @Transactional
    public CafeResponse addCafe(CafeRequest requestCafe) {
        Cafe cafe = Cafe.of(requestCafe.cafeName(), requestCafe.description());
        Cafe savedCafe = cafeRepository.save(cafe);
        return CafeResponse.of(savedCafe);
    }

    @Transactional(readOnly = true)
    public CafeResponse getCafe(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new RuntimeException(""));
        return CafeResponse.of(cafe);
    }

    @Transactional
    public CafeResponse updateCafe(CafeRequest requestCafe, Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new RuntimeException(""));
        cafe.setCafeName(requestCafe.cafeName());
        cafe.setDescription(requestCafe.description());
        return CafeResponse.of(cafe);
    }

    @Transactional
    public void deleteCafe(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new RuntimeException(""));
        cafeRepository.delete(cafe);
    }
}
