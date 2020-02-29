package com.greatmrpark.helper.batch.job;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greatmrpark.helper.batch.service.SchedulerService;
import com.greatmrpark.helper.common.model.code.ApiMessageCode;
import com.greatmrpark.helper.crawler.service.Go1372CrawlerService;

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
    
    @Value("${gmp.ocrdatapath}")
    private static String datapath;

    @Autowired
    SchedulerService schedulerService;

    @Autowired
    Go1372CrawlerService go1372CrawlerService;
    
    /**
     * WEB CRAWLER
     */
    public void process() {
        log.info("start GmpWebCrawlerJob.process-----------------------------------------------------");
        
        crwler1372AltNews();
        
        
        /**
         * job 실행 완료 시간 update
         */
        schedulerService.batchJobSchedulerExecute(jobName);
        
        log.info("end GmpWebCrawlerJob.process-----------------------------------------------------");
    }
    
    /**
     * 1372 알림뉴스
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public void crwler1372AltNews() {

        log.info("start crwler1372AltNews-----------------------------------------------------");

        int totalCount = 0;
        int processCount = 0;
        
        String collection = "altNews";
        ArrayList<HashMap<String, Object>> contents = go1372CrawlerService.post(collection);
        if (!contents.isEmpty() && contents != null && contents.size() > 0) {
            log.debug("contents : {}" , gson.toJson(contents));
        }
        else {
            log.debug("contents : {}" , ApiMessageCode.API_MSG_0008.getValue());
        }
        log.info("end crwler1372AltNews-----------------------------------------------------");
    }
    
    /**
     * 1372 상담조회
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public void crwler1372Counsel() {
        
    }

    /**
     * 1372 정보자료
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public void crwler1372InfoData() {
        
    }

    /**
     * consumernews
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public void crwlerConsumernews() {
        
    }

    /**
     * kcal
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public void crwlerKca() {
        
    }
}
