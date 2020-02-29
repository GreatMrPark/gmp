package com.greatmrpark.helper.crawler.service;

import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greatmrpark.helper.batch.job.GmpWebCrawlerJob;
import com.greatmrpark.helper.common.model.error.ApiCheckedException;
import com.greatmrpark.helper.common.model.error.ApiErrCode;
import com.greatmrpark.helper.crawler.model.CrawlerInfo;

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

    @Autowired
    GmpWebCrawlerJob gmpWebCrawlerJob;
    
    public Boolean crawlerStart(CrawlerInfo params) throws ApiCheckedException {

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
        
        /**
         * 3. DATA 처리
         */
        gmpWebCrawlerJob.crwler1372AltNews();

        /**
         * 4. DATA 결과
         */
        
        return true;
    }
}
