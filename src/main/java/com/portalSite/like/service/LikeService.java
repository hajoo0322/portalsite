package com.portalSite.like.service;

import com.portalSite.cafe.entity.CafeMember;
import com.portalSite.cafe.entity.CafePost;
import com.portalSite.cafe.repository.CafeMemberRepository;
import com.portalSite.cafe.repository.CafePostRepository;
import com.portalSite.like.entity.PostLike;
import com.portalSite.like.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final CafeMemberRepository cafeMemberRepository;
    private final CafePostRepository cafePostRepository;

    @Transactional
    public void doLike(Long cafeId, Long cafePostId, Long cafeMemberId) {
        CafePost foundCafePost = cafePostRepository.findById(cafePostId)
                .orElseThrow(() -> new RuntimeException(""));
        CafeMember foundCafeMember = cafeMemberRepository.findByCafeIdAndMemberId(cafeId, cafeMemberId)
                .orElseThrow(() -> new RuntimeException(""));
        PostLike like = PostLike.of(foundCafePost, foundCafeMember);
        likeRepository.save(like); // 한명이 하나의 포스에 하나만 좋아요 누르는 로직필요
    }

    @Transactional(readOnly = true)
    public Integer countLikes(Long cafePostId) {
        CafePost foundCafePost = cafePostRepository.findById(cafePostId)
                .orElseThrow(() -> new RuntimeException(""));
        return likeRepository.countByCafePost(foundCafePost);
    }

    @Transactional
    public void undoLike(Long likeId) {
        PostLike foundLike = likeRepository.findById(likeId)
                .orElseThrow(() -> new RuntimeException(""));
        likeRepository.delete(foundLike);
    }
}
