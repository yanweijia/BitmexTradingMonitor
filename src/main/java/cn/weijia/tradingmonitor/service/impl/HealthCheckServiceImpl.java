package cn.weijia.tradingmonitor.service.impl;

import cn.weijia.tradingmonitor.platform.BitmexMonitor;
import cn.weijia.tradingmonitor.service.HealthCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * socket 存活检查
 */
@Component
@Slf4j
public class HealthCheckServiceImpl implements HealthCheckService {

    @Resource
    private BitmexMonitor bitmexMonitor;

    @Scheduled(cron = "0/10 * *  * * ? ")
    public void bitmexMonitorCheck() {
        if (!bitmexMonitor.isRunning()) {
            bitmexMonitor.createConnect();
            log.info("reconnecting to Bitmex socket");
        }
    }
}
