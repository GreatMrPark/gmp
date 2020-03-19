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
 *  greatmrpark 2020. 3. 9.
 *
 */	
package com.greatmrpark.utility;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Crawler4jApp {
    
    public static void main(String[] args) throws Exception {
        
        // 데이터 임시저장장소
        String crawlStorageFolder = "/project/data/crawl/root";
        
        // 크롤러 동시 실행 수 지정
        int numberOfCrawlers = 7;
        
        // 1. 크롤링 깊이 (Crawl Depth)
        int maxDepthOfCrawling = 2;
        
        // 2. 크롤링 딜레이 (Politeness)
        int politenessDelay = 500;
        
        // 3. 프록시(Proxy) 
        String proxyHost = "";
        String proxyUsername = "";
        String proxyPassword = "";
        
        CrawlConfig config = new CrawlConfig();
        config.setMaxDepthOfCrawling(maxDepthOfCrawling);   // 시작 URL에서 몇 단계까지 탐색할지 설정
        config.setPolitenessDelay(politenessDelay);         // 동일 호스트에 대한 요청 delay 설정 (ms) 
        config.setCrawlStorageFolder(crawlStorageFolder);   // 크롤러의 데이터 저장 디렉터리 지정
        
        // CrawController 준비하기
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        
        // 크롤링 시작 URL 지정하기
        controller.addSeed("http://www.ics.uci.edu/");
        
        // 크롤링 시작하기
        controller.start(Crawler4jCrawler.class, numberOfCrawlers);
    }  
}

