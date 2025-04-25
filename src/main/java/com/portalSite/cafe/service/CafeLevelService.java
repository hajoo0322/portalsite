package com.portalSite.cafe.service;

import com.portalSite.cafe.dto.CafeLevelRequest;
import com.portalSite.cafe.dto.CafeLevelRequestList;
import com.portalSite.cafe.dto.CafeLevelResponse;
import com.portalSite.cafe.entity.Cafe;
import com.portalSite.cafe.entity.CafeLevel;
import com.portalSite.cafe.repository.CafeLevelRepository;
import com.portalSite.cafe.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CafeLevelService {
    private final CafeLevelRepository cafeLevelRepository;
    private final CafeRepository cafeRepository;

    @Transactional
    public List<CafeLevelResponse> addCafeLevel(CafeLevelRequestList cafeLevelRequest, Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new RuntimeException(""));
        List<CafeLevel> cafeLevelResponses = new ArrayList<>();
        for (CafeLevelRequest cafeLevelList : cafeLevelRequest.cafeLevelRequestList()) {
            CafeLevel cafeLevel = CafeLevel.of(cafe, cafeLevelList.grade(), cafeLevelList.description(), cafeLevelList.autoLevel());
            CafeLevel savedCafeLevel = cafeLevelRepository.save(cafeLevel);
            cafeLevelResponses.add(savedCafeLevel);
        }
        // 리스트 순서대로 저장된다는 보장이 안됨 수정필요
        return cafeLevelResponses.stream().map(CafeLevelResponse::of).toList();
    }

    @Transactional(readOnly = true)
    public List<CafeLevelResponse> getCafeLevel(Long cafeId) {
        List<CafeLevel> cafeLevelList = cafeLevelRepository.findAllByCafeId(cafeId);
        if (cafeLevelList.isEmpty()) {
            throw new RuntimeException("");
        }
        return cafeLevelList.stream().map(CafeLevelResponse::of).toList();
    }

    @Transactional
    public List<CafeLevelResponse> updateCafeLevel(CafeLevelRequestList cafeLevelRequest) {

        return null;
    }
}
