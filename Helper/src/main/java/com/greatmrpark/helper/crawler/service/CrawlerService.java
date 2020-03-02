package com.greatmrpark.helper.crawler.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greatmrpark.helper.batch.job.GmpWebCrawlerJob;
import com.greatmrpark.helper.common.model.db.TbCrawler;
import com.greatmrpark.helper.common.model.error.ApiCheckedException;
import com.greatmrpark.helper.common.model.error.ApiErrCode;
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
    @Autowired CrawlerRepository crawlerRepository;
    
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
            gmpWebCrawlerJob.crwler1372AltNews();
        }
        else if ("1372counsel".equals(crawlerName)) {
            gmpWebCrawlerJob.crwler1372Counsel();
        }
        else if ("1372infodata".equals(crawlerName)) {
            gmpWebCrawlerJob.crwler1372InfoData();
        }
        else if ("kcaboard".equals(crawlerName)) {
            gmpWebCrawlerJob.crwlerKcaBoard();
        }
        else if ("kcaboard".equals(crawlerName)) {
            gmpWebCrawlerJob.crwlerKcaReport();
        }
        else if ("consumernews".equals(crawlerName)) {
            gmpWebCrawlerJob.crwlerConsumerNews();
        }
        else {
            throw new ApiCheckedException(ApiErrCode.API_ERR_0002, crawlerName);
        }

        /**
         * 4. DATA 결과
         */
        
        return true;
    }
}
