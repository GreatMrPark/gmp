/*
 *  Copyright (c) 2020 Great Mr. Park
 *  All right reserved.
 *  This software is the confidential and proprietary information of Great Mr. Park.
 *  You shall not disclose such Confidential Information and
 *  shall use it only in accordance with the terms of the license agreement
 *  you entered into with Great Mr. Park.
 *
 *  Revision History
 *  Author Date Description
 *  ------------------ -------------- ------------------
 *  greatmrpark 2020. 3. 4.
 *
 */	
package com.greatmrpark.helper.crawler.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greatmrpark.helper.common.model.db.TbCrawler;
import com.greatmrpark.helper.common.model.error.ApiCheckedException;
import com.greatmrpark.helper.common.model.error.ApiErrCode;
import com.greatmrpark.helper.common.repository.CrawlerRepository;
import com.greatmrpark.helper.common.utils.CrawlerUtil;
import com.greatmrpark.helper.crawler.client.CrawlerClient;

import lombok.extern.slf4j.Slf4j;

/**
 * 크롤러 스크래핑 엔진
 *
 * <p>
 * com.greatmrpark.helper.crawler.service
 * CrawlerParser.java
 *
 * @history
 * 개정이력(Modification Information)·
 * 수정일   수정자    수정내용
 * ------------------------------------
 * 2020. 3. 4.    greatmrpark     최초작성
 *  
 * @author greatmrpark
 * @since 2020. 3. 4.
 * @version 1.0.0
 */
@Slf4j
@Service
public class CrawlerScraping {
    
    private double ptime = 1000 * 0.5; 
    private Gson gson = new GsonBuilder().create();

    @Value("${gmp.file.images.download}")
    private String imageDownloaPath;
    
    @Value("${gmp.ocr.datapath}")
    private String datapath;

    @Autowired CrawlerRepository crawlerRepository;

    @Autowired CrawlerClient crawlerClient;
    
    public String scrapingHtml(String crawlerName) throws ApiCheckedException {

        if (StringUtils.isBlank(crawlerName)) {
            throw new ApiCheckedException(ApiErrCode.API_ERR_0001, "crawlerName");
        }
        
        /**
         * 3. DATA 처리
         */
        Optional<TbCrawler> tbCrawlerOpt = crawlerRepository.findOptByCrawlerName(crawlerName);
        if (tbCrawlerOpt.isPresent() == false) {
            throw new ApiCheckedException(ApiErrCode.API_ERR_0002, crawlerName);
        }
        TbCrawler crawler = tbCrawlerOpt.get();
        log.debug("tbCrawlerOpt : {}", gson.toJson(tbCrawlerOpt));
        
        return scrapingHtml(crawler);
    }

    public String scrapingHtml(TbCrawler crawler) {
        
        LocalDateTime startDateTime = LocalDateTime.now();

        String url = null;
        String html = null;

        Map<String, Object> urlMap = new HashMap<String, Object>();
        
        ArrayList<String> links = new ArrayList<String>();
        ArrayList<String> pages = new ArrayList<String>();
        ArrayList<HashMap<String, Object>> contents = new ArrayList<HashMap<String, Object>>();
        
        String defaultUrl       = crawler.getDefaultUrl();
        String searchUrl        = crawler.getSearchUrl();
        String collection       = crawler.getCollection();
        String keyword          = crawler.getKeyword();
        String contentMethod    = crawler.getContentMethod();
        String contentType      = crawler.getContentType();
        Integer pageSize        = crawler.getPageSize();
        String pageEl           = crawler.getPageEl();
        String listEl           = crawler.getListEl();
        String bodyEl           = crawler.getBodyEl();
        String titleEl          = crawler.getTitleEl();
        String contentsEl       = crawler.getContentsEl();
        String replyContentsEl  = crawler.getReplyContentsEl();
        String itemEl           = crawler.getItemEl();

        urlMap.put("keyword", keyword);
        urlMap.put("startCount", "0");
        urlMap.put("page", "1");
        url  = CrawlerUtil.messageTemplate(searchUrl, urlMap);
        
        try {
            html = crawlerClient.post(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LocalDateTime endDateTime = LocalDateTime.now();
        Duration duration = Duration.between(startDateTime, endDateTime);
        log.debug("totalCount : {}, 수행시간 : {} Seconds({}~{})" ,links.size(), duration.getSeconds(), startDateTime, endDateTime);
        
        return html;
    }
}
