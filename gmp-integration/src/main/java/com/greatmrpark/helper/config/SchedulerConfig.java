package com.greatmrpark.helper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.greatmrpark.helper.batch.scheduler.DynamicScheduler;
import com.greatmrpark.helper.batch.scheduler.GmpWebCrawlerScheduler;

/**
 * <p>
 * <pre>
 * 
 * SchedulerConfig.java
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
@Configuration
@EnableScheduling
public class SchedulerConfig implements CommandLineRunner {

    @Autowired DynamicScheduler dynamicScheduler;
    @Autowired GmpWebCrawlerScheduler gmpWebCrawlerScheduler;
    
    @Override
    public void run(String... args) {
        dynamicScheduler.startScheduler();
        gmpWebCrawlerScheduler.startScheduler();
    }
}
