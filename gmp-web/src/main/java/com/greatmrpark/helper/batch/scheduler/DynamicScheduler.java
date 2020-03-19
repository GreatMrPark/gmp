package com.greatmrpark.helper.batch.scheduler;

import java.util.Date;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * <pre>
 * 
 * DynamicScheduler.java
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
@Component("DynamicScheduler")
public class DynamicScheduler extends DynamicAbstractScheduler {

    private static final String CRON = "0/7 * * * * ?";
    
    // 실행주기 
    @Override
    public Trigger getTrigger() {
        return new CronTrigger(CRON);
    }
    
    // 실행로직 
    @Override
    public void runner() {
        log.debug("DynamicScheduler : " + new Date());
    }
    
    // 중지체크
    @Override
    public Boolean stop() {
        return false;
    }
    
}
