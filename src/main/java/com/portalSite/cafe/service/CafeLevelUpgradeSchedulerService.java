package com.portalSite.cafe.service;

import com.portalSite.cafe.entity.Cafe;
import com.portalSite.cafe.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CafeLevelUpgradeSchedulerService {

    private final CafeMemberService cafeMemberService;
    private final CafeRepository cafeRepository;

    public void upgradeAllCafeMembersLevel() { // 카페, 회원 규모에 따라 스케줄러 분산 -> 큐 사용 등 개선 필요
        List<Cafe> cafeList = cafeRepository.findAll();
        List<Cafe> upgradeFailedCafeIdList = new ArrayList<>();

        for (Cafe cafe : cafeList) {
            try {
                cafeMemberService.upgradeOneCafeMemberGrade(cafe);
            }catch (Exception e){
                upgradeFailedCafeIdList.add(cafe);
            }
        }

        //실패 케이스 재처리 로직(추후 개선 여지 있음). 비즈니스로직상 실패한 경우일 수도 있기 때문에 2번만 시도
        if(!upgradeFailedCafeIdList.isEmpty()) {
            log.info("등업 1차 실패 카페 등업 재시도 시작");
            for(Cafe cafe : upgradeFailedCafeIdList) {
                try{
                    cafeMemberService.upgradeOneCafeMemberGrade(cafe);
                }catch (Exception e){
                    log.error("재시도 최종 실패 카페 Id: {}", cafe.getId());
                }
            }
        }
    }
}
