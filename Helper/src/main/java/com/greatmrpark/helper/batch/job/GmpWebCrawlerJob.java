package com.greatmrpark.helper.batch.job;

import java.time.format.DateTimeFormatter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greatmrpark.helper.batch.service.SchedulerService;
import com.greatmrpark.helper.crawler.service.CrawlerService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * <pre>
 * 
 * SmgwProvisionBackUpJob.java
 * 개정이력(Modification Information)·
 * 수정일   수정자    수정내용
 * ------------------------------------
 * 2019. 6. 13.    greatmrpark     최초작성
 * </pre>
 *  
 * @author greatmrpark
 * @since 2019. 6. 13.
 * @version 1.0.0
 */
@Slf4j
@Service
public class GmpWebCrawlerJob {

    private Gson gson = new GsonBuilder().create();
    private ModelMapper modelMapper = new ModelMapper();
    private ObjectMapper objectMapper = new ObjectMapper();
    private DateTimeFormatter ldtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss"); 
    private String jobName = "GmpWebCrawlerScheduler";

    @PersistenceContext
    private EntityManager entityManager;
    
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size:1000}")
    private int batchSize;
    
    @Value("${gmp.file.images.download}")
    private static int imageDownloaPath;
    
    @Value("${gmp.ocr.datapath}")
    private static String datapath;

    @Autowired SchedulerService schedulerService;

    @Autowired CrawlerService crawlerService;
    
    /**
     * WEB CRAWLER
     */
    public void process() {
        log.info("start GmpWebCrawlerJob.process-----------------------------------------------------");
        
        try {            
            crawlerService.crwler1372AltNews();
            crawlerService.crwler1372Counsel();
            crawlerService.crwler1372InfoData();
            crawlerService.crwlerKcaBoard();
            crawlerService.crwlerKcaReport();
            crawlerService.crwlerConsumerNews();
            
            /**
             * job 실행 완료 시간 update
             */
            schedulerService.batchJobSchedulerExecute(jobName);
            
        } catch (Exception e) {
            log.error("error : {}", e.getMessage());
        }
        
        log.info("end GmpWebCrawlerJob.process-----------------------------------------------------");
    }
    
}
