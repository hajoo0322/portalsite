package com.portalSite.cafe.service;

import com.portalSite.cafe.dto.CafePostRequest;
import com.portalSite.cafe.dto.CafePostResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CafePostService {
    public CafePostResponse addCafePost(CafePostRequest cafePostRequest, Long cafeId, Long cafeBoardId) {
        return null;
    }

    public List<CafePostResponse> getAllCafePostByCafeId(Long cafeId) {
        return null;
    }

    public CafePostResponse updateCafePost(CafePostRequest cafePostRequest, Long cafePostId) {
        return null;
    }

    public void deleteCafePost(Long cafePostId) {

    }
}
