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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CafeLevelService {
    private final CafeLevelRepository cafeLevelRepository;
    private final CafeRepository cafeRepository;

    @Transactional
    public List<CafeLevelResponse> addCafeLevel(CafeLevelRequestList cafeLevelRequest, Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new RuntimeException(""));
        List<CafeLevel> cafeLevelList = cafeLevelRequest.cafeLevelRequestList().stream()
                .map(req -> CafeLevel.of(cafe, req.grade(), req.description(), req.autoLevel(), req.gradeOrder(),
                        req.visitCount(), req.commentCount(), req.postCount())).toList();
        List<CafeLevel> savedCafeLevelList = cafeLevelRepository.saveAll(cafeLevelList);

        return savedCafeLevelList.stream().map(CafeLevelResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<CafeLevelResponse> getCafeLevel(Long cafeId) {
        List<CafeLevel> cafeLevelList = cafeLevelRepository.findAllByCafeId(cafeId);
        if (cafeLevelList.isEmpty()) {
            throw new RuntimeException("");
        }
        return cafeLevelList.stream().map(CafeLevelResponse::from).toList();
    }

    @Transactional
    public List<CafeLevelResponse> updateCafeLevel(CafeLevelRequestList cafeLevelRequest, Long cafeId) {
        List<CafeLevel> cafeLevelList = cafeLevelRepository.findAllByCafeId(cafeId);
        Cafe cafe = cafeLevelList.get(0).getCafe();
        Map<Integer, CafeLevel> cafeLevelMap = cafeLevelList.stream()
                .collect(Collectors.toMap(CafeLevel::getGradeOrder, Function.identity()));

        List<CafeLevel> levelsToSave = new ArrayList<>();
        Set<Integer> gradeOrdersRequest = new HashSet<>();

        for (CafeLevelRequest levelRequest : cafeLevelRequest.cafeLevelRequestList()) {
            Integer gradeOrder = levelRequest.gradeOrder();
            gradeOrdersRequest.add(gradeOrder);

            CafeLevel target = cafeLevelMap.get(levelRequest.gradeOrder());
            if (target != null) {
                target.update(levelRequest.grade(), levelRequest.description(), levelRequest.autoLevel(),
                        levelRequest.visitCount(), levelRequest.commentCount(), levelRequest.postCount());
                levelsToSave.add(target);
            } else {
                CafeLevel cafeLevel = CafeLevel
                        .of(cafe, levelRequest.grade(), levelRequest.description(), levelRequest.autoLevel(),
                                levelRequest.gradeOrder(), levelRequest.visitCount(), levelRequest.commentCount(),
                                levelRequest.postCount());
                levelsToSave.add(cafeLevel);
            }
        }

        List<CafeLevel> deleteList = cafeLevelMap.entrySet().stream()
                .filter(entry -> !gradeOrdersRequest.contains(entry.getKey()))
                .map(Map.Entry::getValue).toList();

        List<CafeLevel> savedCafeLevelList = cafeLevelRepository.saveAll(levelsToSave);
        cafeLevelRepository.deleteAll(deleteList);
        return savedCafeLevelList.stream().map(CafeLevelResponse::from).toList();
    }

}
