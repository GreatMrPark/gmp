package com.greatmrpark.helper.crawler.service;

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
import com.greatmrpark.helper.batch.job.GmpWebCrawlerJob;
import com.greatmrpark.helper.common.model.code.ApiMessageCode;
import com.greatmrpark.helper.common.model.db.TbCrawler;
import com.greatmrpark.helper.common.model.db.TbCrawlerCollection;
import com.greatmrpark.helper.common.model.error.ApiCheckedException;
import com.greatmrpark.helper.common.model.error.ApiErrCode;
import com.greatmrpark.helper.common.repository.CrawlerCollectionRepository;
import com.greatmrpark.helper.common.repository.CrawlerRepository;
import com.greatmrpark.helper.crawler.model.CrawlerRequest;

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

    @Autowired GmpWebCrawlerJob gmpWebCrawlerJob;

    @Autowired CrawlerCollectionRepository crawlerCollectionRepository;

    @Autowired CrawlerRepository crawlerRepository;
    
    @Autowired Go1372CrawlerService go1372CrawlerService;
    
    @Autowired ConsumernewsCrawlerService consumernewsCrawlerService;
    
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
//        if (StringUtils.isBlank(params.getCrawlerName())) {
//            throw new ApiCheckedException(ApiErrCode.API_ERR_0001, "crawlerName");
//        }
        
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
        if ("1372altnews".equals(crawlerName)) {
            crwler1372AltNews();
        }
        else if ("1372counsel".equals(crawlerName)) {
            crwler1372Counsel();
        }
        else if ("1372infodata".equals(crawlerName)) {
            crwler1372InfoData();
        }
        else if ("kcaboard".equals(crawlerName)) {
            crwlerKcaBoard();
        }
        else if ("kcaboard".equals(crawlerName)) {
            crwlerKcaReport();
        }
        else if ("consumernews".equals(crawlerName)) {
           crwlerConsumerNews();
        }
        else {
            throw new ApiCheckedException(ApiErrCode.API_ERR_0002, crawlerName);
        }

        /**
         * 4. DATA 결과
         */
        
        return true;
    }
    
    /**
     * 1372 알림뉴스
     */
    @Transactional
    public void crwler1372AltNews() throws ApiCheckedException {

        log.info("start crwler1372AltNews-----------------------------------------------------");

        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime nowDate = LocalDateTime.now();
        String crawlerName  = "1372altnews"; // 1372altnews
        
        int totalCount      = 0;
        int succesCount     = 0;
        int failCount       = 0;
        String userId = "greatmrpark";
                
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
    @Transactional
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
                String reply            = (String) map.get("reply");
                
                String analysisContent  = gson.toJson(map);
                String images           = (String) map.get("images");
                String imagesContent    = (String) map.get("imagesContent");

                HashMap<String, String> etcs = (HashMap<String, String>)map.get("etcs");
                String regNo        = etcs.get("접수번호");
                String replyDate    = etcs.get("답변일");
                String regDate      = etcs.get("작성일");
                String item         = etcs.get("품목");
                                
                TbCrawlerCollection tbCrawlerCollection = new TbCrawlerCollection();
                tbCrawlerCollection.setDefaultUrl(defaultUrl);
                tbCrawlerCollection.setSiteName(siteName);
                tbCrawlerCollection.setPageName(pageName);
                tbCrawlerCollection.setLink(link);
                tbCrawlerCollection.setTitle(title);
                tbCrawlerCollection.setContents(contents);
                tbCrawlerCollection.setReply(reply);
                tbCrawlerCollection.setImages(images);
                tbCrawlerCollection.setImagesContent(imagesContent);
                tbCrawlerCollection.setAnalysisContent(analysisContent);
                
                tbCrawlerCollection.setRegNo(regNo);
                tbCrawlerCollection.setReplyDate(replyDate);
                tbCrawlerCollection.setRegDate(regDate);
                tbCrawlerCollection.setItem(item);
                
                tbCrawlerCollection.setAnlsDate(nowDate);
                log.debug("tbCrawlerCollection : ", gson.toJson(tbCrawlerCollection));
                
                Boolean b = saveCrawlerCollection(tbCrawlerCollection);
                if (b) {
                    succesCount++;
                    log.debug("crwler1372Counsel 성공");
                }
                else {
                    failCount++;
                    log.debug("crwler1372Counsel 실패");
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
    @Transactional
    public void crwler1372InfoData() throws ApiCheckedException {

        log.info("start crwler1372InfoData-----------------------------------------------------");
   
        int totalCount      = 0;
        int succesCount     = 0;
        int failCount       = 0;
        
        String userId = "greatmrpark";
        LocalDateTime nowDate = LocalDateTime.now();
        String crawlerName  = "1372infodata";
                
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
                String source           = etcs.get("만족도");
                String regDate          = etcs.get("등록일");
                String satisfaction     = etcs.get("출처");
                String views            = etcs.get("조회");
                String kind             = etcs.get("품종");
                String product          = etcs.get("해당품목");
                String writer           = etcs.get("작성자");
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
                tbCrawlerCollection.setSatisfaction(satisfaction);
                tbCrawlerCollection.setRegDate(regDate);
                tbCrawlerCollection.setSource(source);
                tbCrawlerCollection.setViews(views);
                tbCrawlerCollection.setKind(kind);
                tbCrawlerCollection.setProduct(product);
                tbCrawlerCollection.setWriter(writer);
                tbCrawlerCollection.setAttached(attached);
                tbCrawlerCollection.setAnlsDate(nowDate);
                log.debug("tbCrawlerCollection : ", gson.toJson(tbCrawlerCollection));
                
                Boolean b = saveCrawlerCollection(tbCrawlerCollection);
                if (b) {
                    succesCount++;
                    log.debug("crwler1372InfoData 성공");
                }
                else {
                    failCount++;
                    log.debug("crwler1372InfoData 실패");
                }
            }
            } catch(Exception e) {e.printStackTrace();}
        }
        else {
            log.debug("contents : {}" , ApiMessageCode.API_MSG_0008.getValue());
        }

        log.debug("crwler1372InfoData 총 : {} (성공 : {}, 실패 : {}) 건 저장" , totalCount, succesCount, failCount);
        
        log.info("end crwler1372InfoData-----------------------------------------------------");
    }

    /**
     * kcal 게시판
     */
    @Transactional
    public void crwlerKcaBoard() throws ApiCheckedException {
        
    }

    /**
     * kcal 보도자료
     */
    @Transactional
    public void crwlerKcaReport() throws ApiCheckedException {
        
    }

    /**
     * consumernews 뉴스
     */
    @Transactional
    public void crwlerConsumerNews() throws ApiCheckedException {

        log.info("start crwlerConsumerNews-----------------------------------------------------");

        LocalDateTime startDateTime = LocalDateTime.now();
        
        String userId = "greatmrpark";
        LocalDateTime nowDate = LocalDateTime.now();
        String crawlerName  = "consumernews";
        
        int totalCount      = 0;
        int succesCount     = 0;
        int failCount       = 0;
                
        /**
         * 크롤링 대상 정보 조회
         */
        Optional<TbCrawler> tbCrawlerOpt = crawlerRepository.findOptByCrawlerName(crawlerName);
        if (tbCrawlerOpt.isPresent() == false) {
            throw new ApiCheckedException(ApiErrCode.API_ERR_0002, crawlerName);
        }
        TbCrawler tbCrawler = tbCrawlerOpt.get();
        log.debug("tbCrawler : {}", gson.toJson(tbCrawler));
        
        String defaultUrl = tbCrawler.getDefaultUrl();
        String siteName   = tbCrawler.getSiteName();
        String pageName   = tbCrawler.getPageName();
                
        ArrayList<HashMap<String, Object>> contentList = consumernewsCrawlerService.parserHtml(tbCrawler);
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

                HashMap<String, String> items = (HashMap<String, String>)map.get("items");
                String source           = items.get("만족도");
                String regDate          = items.get("승인");
                String satisfaction     = items.get("출처");
                String views            = items.get("조회");
                String kind             = items.get("품종");
                String product          = items.get("해당품목");
                String writer           = items.get("작성자");
                String email            = items.get("이메일");
                String attached         = items.get("첨부자료");
                                
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
                tbCrawlerCollection.setSatisfaction(satisfaction);
                tbCrawlerCollection.setRegDate(regDate);
                tbCrawlerCollection.setSource(source);
                tbCrawlerCollection.setViews(views);
                tbCrawlerCollection.setKind(kind);
                tbCrawlerCollection.setProduct(product);
                tbCrawlerCollection.setWriter(writer);
                tbCrawlerCollection.setEmail(email);
                tbCrawlerCollection.setAttached(attached);
                tbCrawlerCollection.setAnlsDate(nowDate);
                log.debug("tbCrawlerCollection : ", gson.toJson(tbCrawlerCollection));
                
                Boolean b = saveCrawlerCollection(tbCrawlerCollection);
                if (b) {
                    succesCount++;
                    log.debug("crwlerConsumerNews 성공");
                }
                else {
                    failCount++;
                    log.debug("crwlerConsumerNews 실패");
                }
            }
            } catch(Exception e) {e.printStackTrace();}
        }
        else {
            log.debug("contents : {}" , ApiMessageCode.API_MSG_0008.getValue());
        }

        log.debug("crwlerConsumerNews 총 : {} (성공 : {}, 실패 : {}) 건 저장" , totalCount, succesCount, failCount);

        LocalDateTime endDateTime = LocalDateTime.now();
        Duration duration = Duration.between(startDateTime, endDateTime);

        log.debug("수행시간 : {} Seconds({} ~ {})" , duration.getSeconds(), startDateTime, endDateTime);
        
        log.info("end crwlerConsumerNews-----------------------------------------------------");
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
