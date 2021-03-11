package com.ylwq.scaffold.service.company.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务组件<br/>
 *
 * @Author thymi
 * @Date 2021/3/11
 */
@Component
@Slf4j
public class ScheduleJobs {

    @Value("${spring.application.name}")
    private String serviceName;

    /**
     * 间隔定时任务，单位：ms
     */
    @Scheduled(fixedRate = 60000)
    public void fixedRateJob() {
        log.info("[" + serviceName + "]定时任务：每间隔60秒执行一次");
    }

    @Scheduled(cron = "0 0,6 0,18 ? * ? ")
    public void cronJob1() {
        log.info("[" + serviceName + "]定时任务：每天15:30执行一次");
    }
}
