package com.portalSite.cafe.service;

import com.portalSite.cafe.dto.CafeResponse;
import com.portalSite.cafe.dto.CafeRequest;
import com.portalSite.cafe.entity.Cafe;
import com.portalSite.cafe.entity.CafeLevel;
import com.portalSite.cafe.repository.CafeLevelRepository;
import com.portalSite.cafe.repository.CafeRepository;
import com.portalSite.common.exception.core.DuplicateNameException;
import com.portalSite.common.exception.core.NotFoundException;
import com.portalSite.common.exception.custom.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeService{

    private final CafeRepository cafeRepository;
    private final CafeLevelRepository cafeLevelRepository;

    @Transactional
    public CafeResponse addCafe(CafeRequest requestCafe) {
        if (cafeRepository.existsByCafeName(requestCafe.cafeName())) {
            throw new DuplicateNameException(ErrorCode.DUPLICATE_NAME);
        }
        Cafe cafe = Cafe.of(requestCafe.cafeName(), requestCafe.description());
        Cafe savedCafe = cafeRepository.save(cafe);
        return CafeResponse.from(savedCafe);
    }

    @Transactional(readOnly = true)
    public CafeResponse getCafe(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_NOT_FOUND));
        return CafeResponse.from(cafe);
    }

    @Transactional
    public CafeResponse updateCafe(CafeRequest requestCafe, Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_NOT_FOUND));
        cafe.setCafeName(requestCafe.cafeName());
        cafe.setDescription(requestCafe.description());
        return CafeResponse.from(cafe);
    }

    @Transactional
    public void deleteCafe(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_NOT_FOUND));
        List<CafeLevel> cafeLevelList = cafeLevelRepository.findAllByCafeId(cafeId);
        cafeRepository.delete(cafe);
        cafeLevelRepository.deleteAll(cafeLevelList);
    }

    @Transactional(readOnly = true)
    public void duplicateCafeName(String cafeName) {
        cafeRepository.findByCafeName(cafeName).ifPresent(cafe -> {
            throw new DuplicateNameException(ErrorCode.DUPLICATE_NAME);
        });
    }
}
