package com.portalSite.cafe.repository;

import com.portalSite.cafe.dto.CafeResponse;
import com.portalSite.cafe.dto.RequestCafe;
import com.portalSite.cafe.service.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CafeService{

    private final CafeRepository cafeRepository;

    public CafeResponse addCafe(RequestCafe requestCafe) {
        return null;
    }

    public CafeResponse getCafe(Long cafeId) {
        return null;
    }

    public CafeResponse updateCafe(RequestCafe requestCafe, Long cafeId) {
        return null;
    }

    public void deleteCafe(Long cafeId) {

    }
}
