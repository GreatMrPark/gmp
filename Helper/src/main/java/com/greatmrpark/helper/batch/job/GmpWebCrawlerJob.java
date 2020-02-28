package com.greatmrpark.helper.batch.job;

import java.time.format.DateTimeFormatter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * <pre>
 * com.gelix.gwaadmin.scheduler
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

    @PersistenceContext
    private EntityManager entityManager;
    
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size:1000}")
    private int batchSize;
    
    /**
     * WEB CRAWLER
     * 전체, USE YN : Y, DEL YN : N , STATUS : 
     * Process.
     */
    public void process() {
        log.info("start GmpWebCrawlerJob.process-----------------------------------------------------");
        
        int totalCount = 0;
        int processCount = 0;
        
        crwler1372AltNews();
        
        log.info("{} / {} SUCCESS", processCount, totalCount);
        log.info("end GmpWebCrawlerJob.process-----------------------------------------------------");
    }
    
    /**
     * 1372 알림뉴스
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public void crwler1372AltNews() {

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
