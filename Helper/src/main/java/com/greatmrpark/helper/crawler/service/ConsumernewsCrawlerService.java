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
package com.greatmrpark.helper.crawler.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greatmrpark.helper.common.model.db.TbCrawler;
import com.greatmrpark.helper.common.utils.CrawlerUtil;
import com.greatmrpark.helper.crawler.client.CrawlerClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsumernewsCrawlerService {

    private Gson gson = new GsonBuilder().create();
    private int PTIME = 1000 * 1; 
    private int LPCYCLE = 5;
    
    private String DEFAULT_URL   = "https://www.consumernews.co.kr";
    private String SEARCH_URL    = "https://www.consumernews.co.kr/news/articleList.html";
    private String SC_AREA   = "A";
    private String VIEW_TYPE = "sm";
    private String QUERY_STRING  = "교원";
    private Integer PAGE_SIZE = 10;
    
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
        String pageEl           = crawler.getPageEl();
        String listEl           = crawler.getListEl();
        String bodyEl           = crawler.getBodyEl();
        String titleEl          = crawler.getTitleEl();
        String contentsEl       = crawler.getContentsEl();
        String replyEl          = crawler.getReplyEl();
        String itemEl           = crawler.getItemEl();

        urlMap.put("keyword", keyword);
        urlMap.put("page", "");
        log.debug("urlMap : {}" , gson.toJson(urlMap));
        url  = CrawlerUtil.messageTemplate(searchUrl, urlMap);
        log.debug("url : {}" , url);
        
        try {
            html = crawlerClient.post(url);
            links = parserList(html, defaultUrl, listEl);
            log.debug("links : {}", gson.toJson(links));
            pages = parserPage(html, defaultUrl, pageEl);
            log.debug("pages : {}", gson.toJson(pages));
            if (pages != null && pages.size() > 0) {
                
                for(String p : pages) {

                    CrawlerUtil.sleep(PTIME);

                    if (!"".contentEquals(p)) {
                        
                        urlMap.put("keyword", keyword);
                        urlMap.put("page", p);
                        log.debug("urlMap : {}" , gson.toJson(urlMap));
                        url  = CrawlerUtil.messageTemplate(searchUrl, urlMap);
                        log.debug("url : {}" , url);
                        
                        html = crawlerClient.post(url);
                        links.addAll(parserList(html, defaultUrl, listEl));
                    }
                }
                log.debug("links : {}", gson.toJson(links));
            }
            log.debug("links : {}", gson.toJson(links));
            
            if (!links.isEmpty() && links != null && links.size() > 0) {
                for(String link: links) {
                    
                    CrawlerUtil.sleep(PTIME);
                    
                    contents.add(parserContents(link, crawler));
                    
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
    public ArrayList<String> parserPage(String html, String defaultUrl, String pageEl) {

        Document doc = Jsoup.parse(html);
        Elements pagination = doc.select(pageEl);
        
        // 페이지
        ArrayList<String> pages = new ArrayList<String>();
        if (pagination != null) {
            for(Element e : pagination) {            
                if (!"".equals(e.text().toString())) {
                    pages.add(e.text().toString());
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
    public ArrayList<String> parserList(String html, String defaultUrl, String listEl) {

        Document doc = Jsoup.parse(html);
        Elements contents = doc.select(listEl);
        
        // 목록
        ArrayList<String> links = new ArrayList<String>();
        if (contents != null) {
            for(Element e : contents) {
                Elements linkHref = e.select("a");
                String link = linkHref.attr("href");
                if (!linkHref.attr("href").toLowerCase().contains("http")) {
                    link = defaultUrl + link;                    
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
     * 소비자가만드는신문 > 뉴스
     * @param html
     * @param link
     */
    public HashMap<String, Object> parserContents(String link, TbCrawler crawler) {

        HashMap<String, Object> content = new HashMap<String, Object>();
        try {
            String html = crawlerClient.post(link);
            
            Document doc = Jsoup.parse(html);
//            doc.outputSettings().prettyPrint(false); // 줄바꿈 살림
            Elements body   = doc.select(crawler.getBodyEl());
            String title    = body.select(crawler.getTitleEl()).text().toString();
            String contents = body.select(crawler.getContentsEl()).html().toString();
            
            // 컨덴츠
            HashMap<String, String> etcMap = new HashMap<String, String>();
            Elements rows = body.select(crawler.getItemEl());
            for(Element row : rows) {
                
                Elements cells = row.select("td");
                int cellCount = cells.size();
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
            
            // 이미지 추출
            Elements image = body.select(crawler.getContentsEl() + " img");
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
            }
            
            // OCR : TEXT 추출
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
            content.put("contents", contents);
            content.put("images", images);
            content.put("imagesContent", imagesContent);
            content.put("etcs", etcMap);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return content;
    }
    
    /**
     * TEXT 추출
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 3. 3. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method extractionText
     *
     * @param list
     * @return
     */
    public StringBuffer extractionText(ArrayList<String> images) {

        StringBuffer sb = new StringBuffer();
        if (!images.isEmpty() && images != null && images.size() > 0) {
            for(String imageFullPath : images) {
                sb.append(CrawlerUtil.doOCR(imageFullPath, datapath));
                sb.append("\n");
            }
        }               
        return sb;
    }

}
