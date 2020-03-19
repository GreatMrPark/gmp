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
 *  greatmrpark 2020. 3. 2.
 *
 */	
package com.greatmrpark.web.crawler.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greatmrpark.web.common.model.db.TbCrawler;
import com.greatmrpark.web.common.utils.CrawlerUtil;
import com.greatmrpark.web.crawler.client.CrawlerClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KcaCrawlerService {

    private Gson gson = new GsonBuilder().create();
    private int PTIME = 1; 
    
    private String DEFAULT_URL   = "https://www.kca.go.kr";
    private String SEARCH_URL    = "https://www.kca.go.kr/search/index.do";
    private String[] COLLECTION = {"report", "board"}; // report : 보도자료, webpage : 웹페이지, board : 게시판, menu : 메뉴정도, consumer : 소비자 문제연구
    private String QUERY_STRING  = "교원";
    private String MODE   = "list";
    private String SCODE   = "";
    private Integer PAGE_SIZE    = 10;

    @Value("${gmp.file.images.download}")
    private String imageDownloaPath;
    
    @Value("${gmp.ocr.datapath}")
    private String datapath;
    
    @Autowired
    CrawlerClient crawlerClient;

    public ArrayList<HashMap<String, Object>> post(TbCrawler crawler) {
        
        LocalDateTime startDateTime = LocalDateTime.now();

        String url = null;
        String html = null;
        ArrayList<String> links = new ArrayList<String>();
        ArrayList<String> pages = new ArrayList<String>();
        ArrayList<HashMap<String, Object>> contents = new ArrayList<HashMap<String, Object>>();
        
        String searchUrl  = crawler.getSearchUrl();
        String collection = crawler.getCollection();
        String mode       = "list";
        String keyword = crawler.getKeyword();
        
        url  = searchUrl;
        url += "&collection=" + collection; 
        url += "&kwd=" + keyword;
        
        try {
            html = crawlerClient.post(url);
            links = parserList(html);
            log.debug("links : {}", gson.toJson(links));
            pages = parserPage(html);
            log.debug("pages : {}", gson.toJson(pages));
            if (pages != null && pages.size() > 0) {

                CrawlerUtil.sleep(PTIME);
                
                for(String p : pages) {

                    if (!"".contentEquals(p)) {
                        int page = (Integer.parseInt(p) - 1) * PAGE_SIZE;
                        
                        url  = searchUrl;
                        url += "&collection=" + collection; 
                        url += "&srchopt=" + collection; 
                        url += "&mode=" + mode;
                        url += "&kwd=" + keyword;
                        url += "&page="+ p;
                        
                        html = crawlerClient.post(url);
                        links.addAll(parserList(html));
                    }
                }
                log.debug("links : {}", gson.toJson(links));
            }
            log.debug("links : {}", gson.toJson(links));
            

            if (!links.isEmpty() && links != null && links.size() > 0) {
                for(String link: links) {
                    CrawlerUtil.sleep(PTIME);

                    if (collection=="board") {
                        contents.add(parserBoard(link));
                    }
                    else {
                        contents.add(parserBoard(link));
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("contents : {}", gson.toJson(contents));

        LocalDateTime endDateTime = LocalDateTime.now();
        Duration duration = Duration.between(startDateTime, endDateTime);

        log.debug("startDateTime : {} ~ endDateTime : {}", startDateTime, endDateTime);
        log.debug("totalCount : {}, 실행 : {} Seconds" ,links.size(), duration.getSeconds());
        
        return contents;
    }

    /**
     * 페이지
     * @param html
     */
    public ArrayList<String> parserPage(String html) {

        Document doc = Jsoup.parse(html);
        Elements pagination = doc.select(".pagination a");
        
        // 페이지
        ArrayList<String> pages = new ArrayList<String>();
        if (pagination != null) {
            for(Element e : pagination) {            
                if (!"".equals(e.select("a").text().toString())) {
                    pages.add(e.select("a").text().toString());
                }
            }
        }
        
        return pages;
    }

    /**
     * 목록
     * @param html
     * @param collection
     */
    public ArrayList<String> parserList(String html) {

        Document doc = Jsoup.parse(html);
        Elements contents = doc.select(".searchContent ul li");
        
        // 목록
        ArrayList<String> links = new ArrayList<String>();
        if (contents != null) {
            for(Element e : contents) {
                Elements linkHref = e.select("a");
                String link = linkHref.attr("href");
                if (!linkHref.attr("href").toLowerCase().contains("http")) {
                    link = DEFAULT_URL + link;                    
                }
                
                if (!"".equals(link)) {
                    links.add(link);
                }
            }
        }
        log.debug("links : {}", gson.toJson(links));
        return links;
    }
    
    /**
     * 한국소비자원 > 게시글
     * @param html
     * @param link
     */
    public HashMap<String, Object> parserBoard(String link) {

        HashMap<String, Object> content = new HashMap<String, Object>();
        try {
            String html = crawlerClient.post(link);
            
            Document doc = Jsoup.parse(html);
//            doc.outputSettings().prettyPrint(false); // 줄바꿈 살림
            Elements contents = doc.select(".boardView");
            String title = contents.select("#contentsViewTitle").text().toString();
            String text = contents.select("#contentsViewTitle2").html().toString();
            
            // 컨덴츠
            HashMap<String, String> etcMap = new HashMap<String, String>();
            Elements rows = contents.select("tbody tr");
            for(Element row : rows) {
                
                Elements cells = row.select("td");
                int cellCount = cells.size();
                log.debug("cellCount : {}", cellCount);
                
                if (cellCount > 1 && ((cellCount % 2) == 0)) {
                    for (int i=0; i < cells.size(); i += 2) {
                        Element th = cells.get(i);
                        Element td = cells.get(i+1);
                        log.debug("th : {}", th.text().toString());
                        log.debug("td : {}", td.text().toString());
                        etcMap.put(th.text().toString(), td.text().toString());
                    }
                }
            }
            log.debug("etcMap : {}", gson.toJson(etcMap));
            
            // 이미지 추출
            Elements image = contents.select("#contentsViewTitle2 img");
            ArrayList<String> imgList = new ArrayList<String>();
            String images = "";
            for(Element img : image) {
                String imageFullPath = "";
                String imgUrl = "";
                if (img.attr("src").toLowerCase().contains("http")) {
                    imgUrl = img.attr("src");
                }
                else {
                    imgUrl = DEFAULT_URL + img.attr("src");
                }
                

                if (!"".equals(imgUrl)) {
                    imageFullPath = CrawlerUtil.downloadImage(imageDownloaPath, imgUrl);
                    images += imgUrl + ",";
                    imgList.add(imageFullPath);
                }
                log.debug("images : {}" , images);
                log.debug("imageFullPath : {}" , imageFullPath);
                
            }
            log.debug("imgList : {}" , gson.toJson(imgList));
            
            // OCR
            StringBuffer sb = new StringBuffer();
            if (!imgList.isEmpty() && imgList != null && imgList.size() > 0) {
                for(String imageFullPath : imgList) {
                    log.debug("imageFullPath : {}", imageFullPath);
                    log.debug("datapath : {}", datapath);
                    sb.append(CrawlerUtil.doOCR(imageFullPath, datapath));
                    sb.append("\n");
                }
            }               
            String imagesContent    = sb.toString();

            content.put("link", link);
            content.put("title", title);
            content.put("contents", text);
            content.put("images", images);
            content.put("imagesContent", imagesContent);
            content.put("etcs", etcMap);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.debug("content : {}", gson.toJson(content));
        
        return content;
    } 

    /**
     * 한국소비자원 > 보도자료
     * @param html
     * @param link
     */
    public HashMap<String, Object> parserReport(String link) {

        HashMap<String, Object> content = new HashMap<String, Object>();
        try {
            String html = crawlerClient.post(link);
            
            Document doc = Jsoup.parse(html);
//            doc.outputSettings().prettyPrint(false); // 줄바꿈 살림
            Elements contents = doc.select(".boardView");
            String title = contents.select("#contentsViewTitle").text().toString();
            String text = contents.select("#contentsViewTitle2").html().toString();
            
            // 컨덴츠
            HashMap<String, String> etcMap = new HashMap<String, String>();
            Elements rows = contents.select("tbody tr");
            for(Element row : rows) {
                
                Elements cells = row.select("td");
                int cellCount = cells.size();
                log.debug("cellCount : {}", cellCount);
                
                if (cellCount > 1 && ((cellCount % 2) == 0)) {
                    for (int i=0; i < cells.size(); i += 2) {
                        Element th = cells.get(i);
                        Element td = cells.get(i+1);
                        log.debug("th : {}", th.text().toString());
                        log.debug("td : {}", td.text().toString());
                        etcMap.put(th.text().toString(), td.text().toString());
                    }
                }
            }
            log.debug("etcMap : {}", gson.toJson(etcMap));
            
            // 이미지 추출
            Elements image = contents.select("#contentsViewTitle2 img");
            ArrayList<String> imgList = new ArrayList<String>();
            String images = "";
            for(Element img : image) {
                String imageFullPath = "";
                String imgUrl = "";
                if (img.attr("src").toLowerCase().contains("http")) {
                    imgUrl = img.attr("src");
                }
                else {
                    imgUrl = DEFAULT_URL + img.attr("src");
                }
                

                if (!"".equals(imgUrl)) {
                    imageFullPath = CrawlerUtil.downloadImage(imageDownloaPath, imgUrl);
                    images += imgUrl + ",";
                    imgList.add(imageFullPath);
                }
                log.debug("images : {}" , images);
                log.debug("imageFullPath : {}" , imageFullPath);
                
            }
            log.debug("imgList : {}" , gson.toJson(imgList));
            
            // OCR
            StringBuffer sb = new StringBuffer();
            if (!imgList.isEmpty() && imgList != null && imgList.size() > 0) {
                for(String imageFullPath : imgList) {
                    log.debug("imageFullPath : {}", imageFullPath);
                    log.debug("datapath : {}", datapath);
                    sb.append(CrawlerUtil.doOCR(imageFullPath, datapath));
                    sb.append("\n");
                }
            }               
            String imagesContent    = sb.toString();

            content.put("link", link);
            content.put("title", title);
            content.put("contents", text);
            content.put("images", images);
            content.put("imagesContent", imagesContent);
            content.put("etcs", etcMap);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.debug("content : {}", gson.toJson(content));
        
        return content;
    } 
}
