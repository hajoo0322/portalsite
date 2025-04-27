package com.portalSite.cafe.scheduler;

import com.portalSite.cafe.service.CafeLevelUpgradeSchedulerService;
import com.portalSite.cafe.service.CafeMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpgradeScheduler {

    private final CafeLevelUpgradeSchedulerService schedulerService;

    @Scheduled(cron = "0 0 0 * * *")
    public void upgradeCafeMembersLevel(){
        schedulerService.upgradeAllCafeMembersLevel();
    }
}
