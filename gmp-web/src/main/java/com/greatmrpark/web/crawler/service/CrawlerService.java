package com.greatmrpark.web.crawler.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greatmrpark.web.common.model.code.ApiMessageCode;
import com.greatmrpark.web.common.model.db.TbCrawler;
import com.greatmrpark.web.common.model.db.TbCrawlerCollection;
import com.greatmrpark.web.common.model.error.ApiCheckedException;
import com.greatmrpark.web.common.model.error.ApiErrCode;
import com.greatmrpark.web.common.repository.CrawlerCollectionRepository;
import com.greatmrpark.web.common.repository.CrawlerRepository;
import com.greatmrpark.web.crawler.model.CrawlerRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * 크롤러 서비스
 *
 * <p>
 * com.greatmrpark.helper.crawler.service
 * CrawlerService.java
 *
 * @history
 * 개정이력(Modification Information)·
 * 수정일   수정자    수정내용
 * ------------------------------------
 * 2020. 2. 28.    greatmrpark     최초작성
 *  
 * @author greatmrpark
 * @since 2020. 2. 28.
 * @version 1.0.0
 */
@Slf4j
@Service
public class CrawlerService {

    private Gson gson = new GsonBuilder().create();
    private ModelMapper modelMapper = new ModelMapper();
    private ObjectMapper objectMapper = new ObjectMapper();
    private DateTimeFormatter ldtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss"); 

    @Autowired CrawlerCollectionRepository crawlerCollectionRepository;
    @Autowired CrawlerRepository crawlerRepository;
    @Autowired CrawlerParser crawlerParser;
    
    /**
     * 크롤러 > 목록
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 3. 1. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method crawlerList
     *
     * @param params
     * @return
     * @throws ApiCheckedException
     */
    public Object crawlerList(CrawlerRequest params) throws ApiCheckedException {

        /**
         * 0. 기능설명
         */
        log.debug("크롤러 > 목록");
        
        /**
         *  1. data 수신
         */
        log.debug("params : {}", gson.toJson(params));

        /**
         * 2. data 체크 및 초기화
         */
        if (StringUtils.isBlank(params.getCrawlerName())) {
            throw new ApiCheckedException(ApiErrCode.API_ERR_0001, "crawlerName");
        }
        
        /**
         * 3. DATA 처리
         */
        List<TbCrawler> contents = crawlerRepository.findAll();
        if (contents == null || contents.isEmpty()) {
            params.totalCnt = 0;
            params.resultCnt = 0;
        } 
        else {
            params.totalCnt  = contents.size();
            params.resultCnt = contents.size();
        }

        /**
         * 4. DATA 결과
         */
        List<TbCrawler> results = contents;
        
        return results;
    }

    /**
     * 크롤러 > 보기
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 3. 1. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method crawlerView
     *
     * @param params
     * @return
     * @throws ApiCheckedException
     */
    public Object crawlerView(CrawlerRequest params) throws ApiCheckedException {

        /**
         * 0. 기능설명
         */
        log.debug("크롤러 > 보기");
        
        /**
         *  1. data 수신
         */
        log.debug("params : {}", gson.toJson(params));

        /**
         * 2. data 체크 및 초기화
         */
        if (StringUtils.isBlank(params.getCrawlerName())) {
            throw new ApiCheckedException(ApiErrCode.API_ERR_0001, "crawlerName");
        }
        
        /**
         * 3. DATA 처리
         */
        String crawlerName = params.getCrawlerName();
        Optional<TbCrawler> tbCrawlerOpt = crawlerRepository.findOptByCrawlerName(crawlerName);
        if (tbCrawlerOpt.isPresent() == false) {
            throw new ApiCheckedException(ApiErrCode.API_ERR_0002, crawlerName);
        }
        TbCrawler result = tbCrawlerOpt.get();
        log.debug("tbCrawlerOpt : {}", gson.toJson(tbCrawlerOpt));

        /**
         * 4. DATA 결과
         */
        
        return result;
    }

    /**
     * 크롤러 > 저장
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 3. 1. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method crawlerSave
     *
     * @param params
     * @return
     * @throws ApiCheckedException
     */
    public Object crawlerSave(CrawlerRequest params) throws ApiCheckedException {

        /**
         * 0. 기능설명
         */
        log.debug("크롤러 > 저장");
        
        /**
         *  1. data 수신
         */
        log.debug("params : {}", gson.toJson(params));

        /**
         * 2. data 체크 및 초기화
         */
        if (StringUtils.isBlank(params.getCrawlerName())) {
            throw new ApiCheckedException(ApiErrCode.API_ERR_0001, "crawlerName");
        }
        String userId = "greatmrpark";
        String regId = "greatmrpark";
        String updId = "greatmrpark";
        LocalDateTime nowDate = LocalDateTime.now();
        LocalDateTime regDate = LocalDateTime.now();
        LocalDateTime updDate = LocalDateTime.now();
        
        /**
         * 3. DATA 처리
         */
        TbCrawler crawler =  modelMapper.map(params, TbCrawler.class);
        String crawlerName = crawler.getCrawlerName();
        Optional<TbCrawler> tbCrawlerOpt = crawlerRepository.findOptByCrawlerName(crawlerName);
        
        // 등록
        if (tbCrawlerOpt.isPresent() == false) {
            crawler.setRegId(regId);
            crawler.setRegDate(regDate);
            crawler.setUpdId(updId);
            crawler.setUpdDate(updDate);
        }
        // 수정
        else {
            TbCrawler tbCrawler = tbCrawlerOpt.get();
            crawler.setCrawlerSeq(tbCrawler.getCrawlerSeq());
            crawler.setRegId(tbCrawler.getRegId());
            crawler.setRegDate(tbCrawler.getRegDate());
            crawler.setUpdId(updId);
            crawler.setUpdDate(updDate);
        }
        TbCrawler result = crawlerRepository.saveAndFlush(crawler);
        
        /**
         * 4. DATA 결과
         */
        return result;
    }
    
    /**
     * 크롤러 > 시작
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 3. 1. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method crawlerStart
     *
     * @param params
     * @return
     * @throws ApiCheckedException
     */
    public Boolean crawlerStart(CrawlerRequest params) throws ApiCheckedException {

        /**
         * 0. 기능설명
         */
        log.debug("크롤러 > 시작");
        
        /**
         *  1. data 수신
         */
        log.debug("params : {}", gson.toJson(params));

        /**
         * 2. data 체크 및 초기화
         */
        if (StringUtils.isBlank(params.getCrawlerName())) {
            throw new ApiCheckedException(ApiErrCode.API_ERR_0001, "crawlerName");
        }
        String crawlerName = params.getCrawlerName();
        
        /**
         * 3. DATA 처리
         */
        Optional<TbCrawler> tbCrawlerOpt = crawlerRepository.findOptByCrawlerName(crawlerName);
        if (tbCrawlerOpt.isPresent() == false) {
            throw new ApiCheckedException(ApiErrCode.API_ERR_0002, crawlerName);
        }
        TbCrawler crawler = tbCrawlerOpt.get();
        log.debug("tbCrawlerOpt : {}", gson.toJson(tbCrawlerOpt));
        crawlerParser(crawler);
        
        /**
         * 4. DATA 결과
         */
        
        return true;
    }

    /**
     * Crawler Parser
     */
    @Transactional
    public void crawlerParser(TbCrawler crawler) throws ApiCheckedException {
        
        String userId = "greatmrpark";
        LocalDateTime nowDate = LocalDateTime.now();
        
        int totalCount      = 0;
        int succesCount     = 0;
        int failCount       = 0;
                
        String crawlerName = crawler.getCrawlerName();
        String defaultUrl = crawler.getDefaultUrl();
        String siteName   = crawler.getSiteName();
        String pageName   = crawler.getPageName();
        
        log.info("start crawlerParser({})-----------------------------------------------------", crawlerName);

        LocalDateTime startDateTime = LocalDateTime.now();
                
        ArrayList<HashMap<String, Object>> contentList = crawlerParser.parserHtml(crawler);
        if (!contentList.isEmpty() && contentList != null && contentList.size() > 0) {
            log.debug("contentList : {}" , gson.toJson(contentList));

            totalCount = contentList.size();
            try {
            for (HashMap<String, Object> map : contentList) {
                
                String link             = (String) map.get("link");
                String title            = (String) map.get("title");
                String contents         = (String) map.get("contents");
                String replyContents    = (String) map.get("replyContents");
                
                String analysisContents  = gson.toJson(map);
                String images           = (String) map.get("images");
                String imagesContents    = (String) map.get("imagesContents");

                HashMap<String, String> items = (HashMap<String, String>)map.get("items");
                
                String regNo        = items.get("접수번호");

                String source       = items.get("출처");
                
                String category     = items.get("카테고리");
                String item         = items.get("품목");                
                String kind         = items.get("품종");
                String product      = items.get("해당품목");

                String satisfaction = items.get("만족도");
                
                String[] viewsKey = {"조회","조회수"};
                String views      = "";
                for (int i=0; i<viewsKey.length; i++) {
                    if (items.containsKey(viewsKey[i])) {
                        views      = items.get(viewsKey[i]);
                    }
                }

                String writer       = items.get("작성자");
                
                String email        = items.get("이메일");
                
                String[] regDateKey = {"등록일","작성일","승인"};
                String regDate      = "";
                for (int i=0; i<regDateKey.length; i++) {
                    if (items.containsKey(regDateKey[i])) {
                        regDate      = items.get(regDateKey[i]);
                    }
                }

                String replyDate    = items.get("답변일");

                String[] attachedKey = {"첨부자료","첨부파일"};
                String attached = "";
                for (int i=0; i<attachedKey.length; i++) {
                    if (items.containsKey(attachedKey[i])) {
                        attached      = items.get(attachedKey[i]);
                    }
                }
                                                                
                TbCrawlerCollection tbCrawlerCollection = new TbCrawlerCollection();
                tbCrawlerCollection.setDefaultUrl(defaultUrl);
                tbCrawlerCollection.setSiteName(siteName);
                tbCrawlerCollection.setPageName(pageName);
                tbCrawlerCollection.setLink(link);
                tbCrawlerCollection.setTitle(title);
                tbCrawlerCollection.setContents(contents);
                tbCrawlerCollection.setReplyContents(replyContents);
                tbCrawlerCollection.setImages(images);
                tbCrawlerCollection.setImagesContents(imagesContents);
                tbCrawlerCollection.setAnalysisContents(analysisContents);

                tbCrawlerCollection.setRegNo(regNo);
                tbCrawlerCollection.setSource(source);
                tbCrawlerCollection.setCategory(category);
                tbCrawlerCollection.setItem(item);
                tbCrawlerCollection.setKind(kind);
                tbCrawlerCollection.setProduct(product);
                
                tbCrawlerCollection.setSatisfaction(satisfaction);
                tbCrawlerCollection.setViews(views);

                tbCrawlerCollection.setWriter(writer);
                tbCrawlerCollection.setEmail(email);
                
                tbCrawlerCollection.setRegDate(regDate);                
                tbCrawlerCollection.setReplyDate(replyDate);                
                tbCrawlerCollection.setAttached(attached);                
                tbCrawlerCollection.setAnlsDate(nowDate);
                
                Boolean b = saveCrawlerCollection(tbCrawlerCollection);
                if (b) {
                    succesCount++;
                    log.debug("crawlerParser 성공");
                }
                else {
                    failCount++;
                    log.debug("crawlerParser 실패");
                }
            }
            } catch(Exception e) {e.printStackTrace();}
        }
        else {
            log.debug("contents : {}" , ApiMessageCode.API_MSG_0008.getValue());
        }

        log.info("crawlerParser({}) 총 : {} (성공 : {}, 실패 : {}) 건 저장" , crawlerName, totalCount, succesCount, failCount);
        
        /**
         * 수집일 갱신
         */
        crawler.setCollectDate(nowDate);
        TbCrawler tbCrawlerSave = crawlerRepository.saveAndFlush(crawler);

        LocalDateTime endDateTime = LocalDateTime.now();
        Duration duration = Duration.between(startDateTime, endDateTime);

        log.info("{} 수행시간 : {} Seconds({} ~ {})" , crawlerName, duration.getSeconds(), startDateTime, endDateTime);
        
        log.info("end crawlerParser({})-----------------------------------------------------", crawlerName);
    }

    /**
     * 크롤링 수집 정보 존재 유무
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 3. 4. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method getCrawlerCollectionByLink
     *
     * @param link
     * @param crawler
     * @return
     */
    public Boolean getCrawlerCollectionByLink(String link, TbCrawler crawler) {
        Boolean b = true;
        Optional<TbCrawlerCollection> tbCrawlerCollectionOpt = crawlerCollectionRepository.findFirstByLink(link);

        if (tbCrawlerCollectionOpt.isPresent()==true) {
            b = false;
            
            // 답글이면
            if (crawler.getReplyYn()) {
                TbCrawlerCollection tbCrawlerCollection= tbCrawlerCollectionOpt.get();
                if ("".contentEquals(tbCrawlerCollection.getRegDate())) {
                    b = true;
                }
            }
        }

        return b;
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
        log.debug("tbCrawlerCollection : {}", gson.toJson(tbCrawlerCollection));
        TbCrawlerCollection tbCrawlerCollectionSave = crawlerCollectionRepository.saveAndFlush(tbCrawlerCollection);
        if (tbCrawlerCollectionSave == null) {
            return false;
        } 
        else {
            return true;
        }
    }   
}
