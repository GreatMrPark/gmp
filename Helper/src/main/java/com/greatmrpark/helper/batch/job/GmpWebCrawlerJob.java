package com.greatmrpark.helper.batch.job;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

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
import com.greatmrpark.helper.common.model.db.TbCrawler;
import com.greatmrpark.helper.common.model.db.TbCrawlerCollection;
import com.greatmrpark.helper.common.model.error.ApiCheckedException;
import com.greatmrpark.helper.common.model.error.ApiErrCode;
import com.greatmrpark.helper.common.repository.CrawlerCollectionRepository;
import com.greatmrpark.helper.common.repository.CrawlerRepository;
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
    
    @Value("${gmp.ocr.datapath}")
    private static String datapath;

    @Autowired
    SchedulerService schedulerService;

    @Autowired
    Go1372CrawlerService go1372CrawlerService;

    @Autowired
    CrawlerCollectionRepository crawlerCollectionRepository;

    @Autowired
    CrawlerRepository crawlerRepository;
    
    /**
     * WEB CRAWLER
     */
    public void process() {
        log.info("start GmpWebCrawlerJob.process-----------------------------------------------------");
        
        try {
            
            crwler1372AltNews();
            crwler1372Counsel();
            
            /**
             * job 실행 완료 시간 update
             */
            schedulerService.batchJobSchedulerExecute(jobName);
            
        } catch (Exception e) {
            log.error("error : {}", e.getMessage());
        }
        
        log.info("end GmpWebCrawlerJob.process-----------------------------------------------------");
    }
    
    /**
     * 1372 알림뉴스
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public void crwler1372AltNews() throws ApiCheckedException {

        log.info("start crwler1372AltNews-----------------------------------------------------");
   
        int totalCount      = 0;
        int succesCount     = 0;
        int failCount       = 0;
        String userId = "greatmrpark";
        LocalDateTime nowDate = LocalDateTime.now();
        String crawlerName  = "1372altnews"; // 1372altnews
                
        /**
         * 크롤링 대상 정보 조회
         */
        Optional<TbCrawler> tbCrawlerOpt = crawlerRepository.findOptByCrawlerName(crawlerName);
        if (tbCrawlerOpt.isPresent() == false) {
            throw new ApiCheckedException(ApiErrCode.API_ERR_0002, crawlerName);
        }
        TbCrawler tbCrawler = tbCrawlerOpt.get();
        log.debug("tbCrawlerOpt : {}", gson.toJson(tbCrawlerOpt));
        
        String defaultUrl = tbCrawler.getDefaultUrl();
        String siteName   = tbCrawler.getSiteName();
        String pageName   = tbCrawler.getPageName();
                
        ArrayList<HashMap<String, Object>> contentList = go1372CrawlerService.post(tbCrawler);
        if (!contentList.isEmpty() && contentList != null && contentList.size() > 0) {
            log.debug("contentList : {}" , gson.toJson(contentList));

            totalCount = contentList.size();
            try {
            for (HashMap<String, Object> map : contentList) {
                
                String link             = (String) map.get("link");
                String title            = (String) map.get("title");
                String contents         = (String) map.get("contents");
                
                String analysisContent  = gson.toJson(map);
                String images           = (String) map.get("images");
                String imagesContent    = (String) map.get("imagesContent");

                HashMap<String, String> etcs = (HashMap<String, String>)map.get("etcs");
                String category         = etcs.get("카테고리");
                String regDate          = etcs.get("등록일");
                String source           = etcs.get("출처");
                String views            = etcs.get("조회");
                String attached         = etcs.get("첨부자료");
                                
                TbCrawlerCollection tbCrawlerCollection = new TbCrawlerCollection();
                tbCrawlerCollection.setDefaultUrl(defaultUrl);
                tbCrawlerCollection.setSiteName(siteName);
                tbCrawlerCollection.setPageName(pageName);
                tbCrawlerCollection.setLink(link);
                tbCrawlerCollection.setTitle(title);
                tbCrawlerCollection.setContents(contents);
                tbCrawlerCollection.setImages(images);
                tbCrawlerCollection.setImagesContent(imagesContent);
                tbCrawlerCollection.setAnalysisContent(analysisContent);
                tbCrawlerCollection.setCategory(category);
                tbCrawlerCollection.setRegDate(regDate);
                tbCrawlerCollection.setSource(source);
                tbCrawlerCollection.setViews(views);
                tbCrawlerCollection.setAttached(attached);
                tbCrawlerCollection.setAnlsDate(nowDate);
                log.debug("tbCrawlerCollection : ", gson.toJson(tbCrawlerCollection));
                
                Boolean b = saveCrawlerCollection(tbCrawlerCollection);
                if (b) {
                    succesCount++;
                    log.debug("crwler1372AltNews 성공");
                }
                else {
                    failCount++;
                    log.debug("crwler1372AltNews 실패");
                }
            }
            } catch(Exception e) {e.printStackTrace();}
        }
        else {
            log.debug("contents : {}" , ApiMessageCode.API_MSG_0008.getValue());
        }

        log.debug("crwler1372AltNews 총 : {} (성공 : {}, 실패 : {}) 건 저장" , totalCount, succesCount, failCount);
        
        /**
         * 수집일 갱신
         */
        tbCrawler.setCollectDate(nowDate);
        tbCrawler.setUpdId(userId);
        tbCrawler.setUpdDate(nowDate);
        crawlerRepository.saveAndFlush(tbCrawler);
        
        log.info("end crwler1372AltNews-----------------------------------------------------");
    }
    
    /**
     * 1372 상담조회
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public void crwler1372Counsel() throws ApiCheckedException {


        log.info("start crwler1372Counsel-----------------------------------------------------");
   
        int totalCount      = 0;
        int succesCount     = 0;
        int failCount       = 0;
        
        String userId = "greatmrpark";
        LocalDateTime nowDate = LocalDateTime.now();
        String crawlerName  = "1372counsel"; // 1372altnews
                
        /**
         * 크롤링 대상 정보 조회
         */
        Optional<TbCrawler> tbCrawlerOpt = crawlerRepository.findOptByCrawlerName(crawlerName);
        if (tbCrawlerOpt.isPresent() == false) {
            throw new ApiCheckedException(ApiErrCode.API_ERR_0002, crawlerName);
        }
        TbCrawler tbCrawler = tbCrawlerOpt.get();
        log.debug("tbCrawlerOpt : {}", gson.toJson(tbCrawlerOpt));
        
        String defaultUrl = tbCrawler.getDefaultUrl();
        String siteName   = tbCrawler.getSiteName();
        String pageName   = tbCrawler.getPageName();
                
        ArrayList<HashMap<String, Object>> contentList = go1372CrawlerService.post(tbCrawler);
        if (!contentList.isEmpty() && contentList != null && contentList.size() > 0) {
            log.debug("contentList : {}" , gson.toJson(contentList));

            totalCount = contentList.size();
            try {
            for (HashMap<String, Object> map : contentList) {
                
                String link             = (String) map.get("link");
                String title            = (String) map.get("title");
                String contents         = (String) map.get("contents");
                
                String analysisContent  = gson.toJson(map);
                String images           = (String) map.get("images");
                String imagesContent    = (String) map.get("imagesContent");

                HashMap<String, String> etcs = (HashMap<String, String>)map.get("etcs");
                String category         = etcs.get("카테고리");
                String regDate          = etcs.get("등록일");
                String source           = etcs.get("출처");
                String views            = etcs.get("조회");
                String attached         = etcs.get("첨부자료");
                                
                TbCrawlerCollection tbCrawlerCollection = new TbCrawlerCollection();
                tbCrawlerCollection.setDefaultUrl(defaultUrl);
                tbCrawlerCollection.setSiteName(siteName);
                tbCrawlerCollection.setPageName(pageName);
                tbCrawlerCollection.setLink(link);
                tbCrawlerCollection.setTitle(title);
                tbCrawlerCollection.setContents(contents);
                tbCrawlerCollection.setImages(images);
                tbCrawlerCollection.setImagesContent(imagesContent);
                tbCrawlerCollection.setAnalysisContent(analysisContent);
                tbCrawlerCollection.setCategory(category);
                tbCrawlerCollection.setRegDate(regDate);
                tbCrawlerCollection.setSource(source);
                tbCrawlerCollection.setViews(views);
                tbCrawlerCollection.setAttached(attached);
                tbCrawlerCollection.setAnlsDate(nowDate);
                log.debug("tbCrawlerCollection : ", gson.toJson(tbCrawlerCollection));
                
                Boolean b = saveCrawlerCollection(tbCrawlerCollection);
                if (b) {
                    succesCount++;
                    log.debug("crwler1372AltNews 성공");
                }
                else {
                    failCount++;
                    log.debug("crwler1372AltNews 실패");
                }
            }
            } catch(Exception e) {e.printStackTrace();}
        }
        else {
            log.debug("contents : {}" , ApiMessageCode.API_MSG_0008.getValue());
        }

        log.debug("crwler1372Counsel 총 : {} (성공 : {}, 실패 : {}) 건 저장" , totalCount, succesCount, failCount);
        
        log.info("end crwler1372Counsel-----------------------------------------------------");
    }

    /**
     * 1372 정보자료
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public void crwler1372InfoData() throws ApiCheckedException {
        
    }

    /**
     * consumernews
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public void crwlerConsumernews() throws ApiCheckedException {
        
    }

    /**
     * kcal
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public void crwlerKca() throws ApiCheckedException {
        
    }
    
    /**
     * 크롤링 수집 정보 저장
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 2. 29. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method saveCrawlerCollection
     *
     * @param tbCrawlerCollection
     * @return
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public Boolean saveCrawlerCollection(TbCrawlerCollection tbCrawlerCollection) {
        TbCrawlerCollection tbCrawlerCollectionSave = crawlerCollectionRepository.saveAndFlush(tbCrawlerCollection);
        if (tbCrawlerCollectionSave == null) {
            return false;
        } 
        else {
            return true;
        }
    }
}
