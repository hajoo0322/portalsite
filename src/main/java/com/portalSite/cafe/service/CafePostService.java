package com.portalSite.cafe.service;

import com.portalSite.cafe.dto.CafePostRequest;
import com.portalSite.cafe.dto.CafePostResponse;
import com.portalSite.cafe.entity.Cafe;
import com.portalSite.cafe.entity.CafeBoard;
import com.portalSite.cafe.entity.CafeMember;
import com.portalSite.cafe.entity.CafePost;
import com.portalSite.cafe.repository.CafeBoardRepository;
import com.portalSite.cafe.repository.CafeMemberRepository;
import com.portalSite.cafe.repository.CafePostRepository;
import com.portalSite.cafe.repository.CafeRepository;
import com.portalSite.common.exception.core.NotFoundException;
import com.portalSite.common.exception.custom.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafePostService {

    private final CafePostRepository cafePostRepository;
    private final CafeBoardRepository cafeBoardRepository;
    private final CafeRepository cafeRepository;
    private final CafeMemberRepository cafeMemberRepository;

    @Transactional
    public CafePostResponse addCafePost(CafePostRequest cafePostRequest, Long cafeId, Long cafeBoardId, Long memberId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_NOT_FOUND));
        CafeBoard cafeBoard = cafeBoardRepository.findById(cafeBoardId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_BOARD_NOT_FOUND));
        CafeMember cafeMember = cafeMemberRepository.findByCafeIdAndMemberId(cafeId, memberId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_MEMBER_NOT_FOUND));
        CafePost cafePost = CafePost.of(cafe, cafeBoard, cafeMember, cafePostRequest.title(), cafePostRequest.description());
        CafePost savedCafePost = cafePostRepository.save(cafePost);
        return CafePostResponse.from(savedCafePost);
    }

    @Transactional(readOnly = true)
    public List<CafePostResponse> getAllCafePostByCafeId(Long cafeId) {
        List<CafePost> cafePostList = cafePostRepository.findAllByCafeId(cafeId);
        if (cafePostList.isEmpty()) {
            throw new RuntimeException("");
        }
        return cafePostList.stream().map(CafePostResponse::from).toList();
    }

    @Transactional
    public CafePostResponse updateCafePost(CafePostRequest cafePostRequest, Long cafePostId) {
        CafePost cafePost = cafePostRepository.findById(cafePostId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_POST_NOT_FOUND));
        cafePost.update(cafePostRequest);
        CafePost savedCafePost = cafePostRepository.save(cafePost);
        return CafePostResponse.from(savedCafePost);
    }
    @Transactional
    public void deleteCafePost(Long cafePostId) {
        CafePost cafePost = cafePostRepository.findById(cafePostId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_POST_NOT_FOUND));
        cafePostRepository.delete(cafePost);
    }
}
