package com.greatmrpark.helper.batch.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.greatmrpark.common.scheduler.DynamicAbstractScheduler;
import com.greatmrpark.helper.batch.job.GmpWebCrawlerJob;
import com.greatmrpark.helper.batch.service.SchedulerService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * <pre>
 * 
 * GmpWebCrawlerScheduler.java
 * 개정이력(Modification Information)·
 * 수정일   수정자    수정내용
 * ------------------------------------
 * 2019. 7. 1.    greatmrpark     최초작성
 * </pre>
 *  
 * @author greatmrpark
 * @since 2019. 7. 1.
 * @version 1.0.0
 */
@Slf4j
@Component("GmpWebCrawlerScheduler")
public class GmpWebCrawlerScheduler extends DynamicAbstractScheduler {

    private static final String CRON = "0 3 3 * * ?";
    private static final String JOB_NAME = "GmpWebCrawlerScheduler";

    @Autowired
    SchedulerService schedulerService;

    @Autowired
    private GmpWebCrawlerJob gmpWebCrawlerJob;
    
    /**
     * 실행주기
     */ 
    @Override
    public Trigger getTrigger() {
        return new CronTrigger(schedulerService.getBatchJobCron(JOB_NAME, CRON));
    }
    
    /**
     * 실행로직 
     */ 
    @Override
    public void runner() {
        log.debug("GMP  Web Crawler Scheduler start ==========");
        try {
            log.debug("GMP  Web Crawler Job start---------------------------------------");
            gmpWebCrawlerJob.process();
            log.debug("GMP  Web Crawler Job end---------------------------------------");
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        log.debug("GMP  Web Crawler Scheduler stop ==========");
    }

    /**
     * 실행중지
     */
    @Override
    public Boolean stop() {
        return  schedulerService.getBatchJobOn(JOB_NAME);
    }
}
