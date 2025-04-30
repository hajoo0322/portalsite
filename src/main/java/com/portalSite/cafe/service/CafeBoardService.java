package com.portalSite.cafe.service;

import com.portalSite.cafe.dto.CafeBoardRequest;
import com.portalSite.cafe.dto.CafeBoardResponse;
import com.portalSite.cafe.entity.Cafe;
import com.portalSite.cafe.entity.CafeBoard;
import com.portalSite.cafe.repository.CafeBoardRepository;
import com.portalSite.cafe.repository.CafeRepository;
import com.portalSite.common.exception.core.DuplicateNameException;
import com.portalSite.common.exception.core.NotFoundException;
import com.portalSite.common.exception.custom.ErrorCode;
import com.sun.jdi.request.DuplicateRequestException;
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
        if (cafeBoardRepository.existsByBoardName(cafeBoardRequest.boardName())) {
            throw new DuplicateNameException(ErrorCode.DUPLICATE_NAME);
        }
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_NOT_FOUND));
        CafeBoard cafeBoard = CafeBoard.of(cafe, cafeBoardRequest.boardName());
        CafeBoard savedCafeBoard = cafeBoardRepository.save(cafeBoard);
        return CafeBoardResponse.from(savedCafeBoard);
    }

    @Transactional(readOnly = true)
    public List<CafeBoardResponse> getAllCafeBoard(Long cafeId) {
        List<CafeBoard> cafeBoardList = cafeBoardRepository.findAllByCafeId(cafeId);
        return cafeBoardList.stream().map(CafeBoardResponse::from).toList();
    }

    @Transactional
    public CafeBoardResponse updateCafeBoard(CafeBoardRequest cafeBoardRequest, Long cafeBoardId) {
        CafeBoard cafeBoard = cafeBoardRepository.findById(cafeBoardId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_BOARD_NOT_FOUND));
        cafeBoard.update(cafeBoardRequest);
        CafeBoard savedCafeBoard = cafeBoardRepository.save(cafeBoard);
        return CafeBoardResponse.from(savedCafeBoard);
    }

    @Transactional
    public void deleteCafeBoard(Long cafeBoardId) {
        CafeBoard cafeBoard = cafeBoardRepository.findById(cafeBoardId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_BOARD_NOT_FOUND));
        cafeBoardRepository.delete(cafeBoard);
    }

    @Transactional
    public void duplicateCafeBoard(Long cafeId, String boardName) {
        cafeBoardRepository.findByBoardNameAndCafeId(boardName,cafeId).ifPresent(cafeBoard -> {
            throw new DuplicateNameException(ErrorCode.DUPLICATE_NAME);
        });
    }
}
