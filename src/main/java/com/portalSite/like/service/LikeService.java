package com.portalSite.like.service;

import com.portalSite.cafe.entity.CafeMember;
import com.portalSite.cafe.entity.CafePost;
import com.portalSite.cafe.repository.CafeMemberRepository;
import com.portalSite.cafe.repository.CafePostRepository;
import com.portalSite.like.entity.Like;
import com.portalSite.like.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final CafeMemberRepository cafeMemberRepository;
    private final CafePostRepository cafePostRepository;

    @Transactional
    public void doLike(Long cafePostId, Long cafeMemberId) {
        CafePost foundCafePost = cafePostRepository.findById(cafePostId)
                .orElseThrow(() -> new RuntimeException(""));
        CafeMember foundCafeMember = cafeMemberRepository.findById(cafeMemberId)
                .orElseThrow(() -> new RuntimeException(""));
        Like like = Like.of(foundCafePost, foundCafeMember);
        likeRepository.save(like);
    }

    @Transactional(readOnly = true)
    public Integer countLikes(Long cafePostId) {
        CafePost foundCafePost = cafePostRepository.findById(cafePostId)
                .orElseThrow(() -> new RuntimeException(""));
        return likeRepository.countByCafePost(foundCafePost);
    }

    @Transactional
    public void undoLike(Long likeId) {
        Like foundLike = likeRepository.findById(likeId)
                .orElseThrow(() -> new RuntimeException(""));
        likeRepository.delete(foundLike);
    }
}
