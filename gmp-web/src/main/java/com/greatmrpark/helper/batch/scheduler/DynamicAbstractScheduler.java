package com.greatmrpark.helper.batch.scheduler;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * <p>
 * <pre>
 * 
 * DynamicAbstractScheduler.java
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
public abstract class DynamicAbstractScheduler {
    private ThreadPoolTaskScheduler scheduler;

    /**
     * 중지
     * <pre>
     * 
     * ------------------------------------
     * 2019. 7. 1.    greatmrpark     최초작성
     * </pre>
     *
     * @Method : stopScheduler
     *
     */
    public void stopScheduler() {
        if (null != scheduler) {
            scheduler.shutdown();
        }
    }
      
    /**
     * 시작
     * <pre>
     * 
     * ------------------------------------
     * 2019. 7. 1.    greatmrpark     최초작성
     * </pre>
     *
     * @Method : startScheduler
     *
     */
    public void startScheduler() {
        stopScheduler();        
        
        if (stop()) {
            scheduler = new ThreadPoolTaskScheduler();
            scheduler.initialize();
            scheduler.schedule(this::runner, getTrigger());
        }
    }
    
    /**
     * 주기변경
     * <pre>
     * 
     * ------------------------------------
     * 2019. 7. 1.    greatmrpark     최초작성
     * </pre>
     *
     * @Method : changeTrigger
     *
     * @param trigger
     */
    public void changeTrigger(Trigger trigger) {
        startScheduler();
    }
 
    public abstract Boolean stop();
 
    public abstract void runner();
 
    public abstract Trigger getTrigger();
}
