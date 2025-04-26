package com.portalSite.cafe.service;

import com.portalSite.cafe.dto.CafeBoardRequest;
import com.portalSite.cafe.dto.CafeBoardResponse;
import com.portalSite.cafe.entity.Cafe;
import com.portalSite.cafe.entity.CafeBoard;
import com.portalSite.cafe.repository.CafeBoardRepository;
import com.portalSite.cafe.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeBoardService {

    private final CafeBoardRepository cafeBoardRepository;
    private final CafeRepository cafeRepository;

    @Transactional
    public CafeBoardResponse addCafeBoard(CafeBoardRequest cafeBoardRequest, Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new RuntimeException(""));
        CafeBoard cafeBoard = CafeBoard.of(cafe, cafeBoardRequest.boardName());
        CafeBoard savedCafeBoard = cafeBoardRepository.save(cafeBoard);
        return CafeBoardResponse.from(savedCafeBoard);
    }

    @Transactional(readOnly = true)
    public List<CafeBoardResponse> getAllCafeBoard(Long cafeId) {
        List<CafeBoard> cafeBoardList = cafeBoardRepository.findAllByCafeId(cafeId);
        if (cafeBoardList.isEmpty()) {
            throw new RuntimeException("");
        }

        return cafeBoardList.stream().map(CafeBoardResponse::from).toList();
    }

    @Transactional
    public CafeBoardResponse updateCafeBoard(CafeBoardRequest cafeBoardRequest, Long cafeBoardId) {
        CafeBoard cafeBoard = cafeBoardRepository.findById(cafeBoardId).orElseThrow(() -> new RuntimeException(""));
        cafeBoard.update(cafeBoardRequest);
        CafeBoard savedCafeBoard = cafeBoardRepository.save(cafeBoard);
        return CafeBoardResponse.from(savedCafeBoard);
    }

    @Transactional
    public void deleteCafeBoard(Long cafeBoardId) {
        CafeBoard cafeBoard = cafeBoardRepository.findById(cafeBoardId).orElseThrow(() -> new RuntimeException(""));
        cafeBoardRepository.delete(cafeBoard);
    }
}
