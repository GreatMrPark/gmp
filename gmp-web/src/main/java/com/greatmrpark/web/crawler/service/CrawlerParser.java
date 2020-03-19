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
package com.greatmrpark.web.crawler.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

/**
 * 크롤러 파싱 엔진
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
public class CrawlerParser {
    
    private double ptime = 1000 * 0.5; 
    private Gson gson = new GsonBuilder().create();

    @Value("${gmp.file.images.download}")
    private String imageDownloaPath;
    
    @Value("${gmp.ocr.datapath}")
    private String datapath;

    @Autowired CrawlerClient crawlerClient;
    @Autowired CrawlerService crawlerService;

    public ArrayList<HashMap<String, Object>> parserHtml(TbCrawler crawler) {
        
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
            links = parserList(html, crawler);
            pages = parserPage(html, crawler);
            if (pages != null && !pages.isEmpty() && pages.size() > 0) {
                
                for(String page : pages) {

                    CrawlerUtil.sleep(ptime);

                    if (StringUtils.isNoneBlank(page)) {
                        
                        String startCount =  Integer.toString((Integer.parseInt(page) - 1) * pageSize);
                        
                        urlMap.put("keyword", keyword);
                        urlMap.put("startCount", startCount);
                        urlMap.put("page", page);
                        url  = CrawlerUtil.messageTemplate(searchUrl, urlMap);
                        
                        html = crawlerClient.post(url);
                        links.addAll(parserList(html, crawler));
                    }
                }
            }
            
            if (!links.isEmpty() && links != null && links.size() > 0) {
                for(String link: links) {
                    
                    CrawlerUtil.sleep(ptime);
                    
                    contents.add(parserContents(link, crawler));
                    
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        LocalDateTime endDateTime = LocalDateTime.now();
        Duration duration = Duration.between(startDateTime, endDateTime);
        log.debug("totalCount : {}, 수행시간 : {} Seconds({}~{})" ,links.size(), duration.getSeconds(), startDateTime, endDateTime);
        
        return contents;
    }

    /**
     * 페이징
     * @param html
     */
    public ArrayList<String> parserPage(String html, TbCrawler crawler) {
        
        String defaultUrl= crawler.getDefaultUrl();
        String pageEl   = crawler.getPageEl();

        ArrayList<String> pages = new ArrayList<String>();

        if (StringUtils.isNoneBlank(html)) { 
            Document doc = Jsoup.parse(html);
            if (StringUtils.isNoneBlank(pageEl)) {            
                Elements pagination = doc.select(pageEl);
                if (pagination != null) {
                    for(Element e : pagination) {      
                        String page = e.text().toString();
                        if (StringUtils.isNoneBlank(page)) {
                            if (CrawlerUtil.isStringDouble(page)) {
                                pages.add(page);
                            }
                        }
                    }
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
    public ArrayList<String> parserList(String html, TbCrawler crawler) {
        String defaultUrl= crawler.getDefaultUrl();
        String contentsUrl = crawler.getContentsUrl();
        String listEl   = crawler.getListEl();
        
        log.debug("listEl : {}", listEl);
        
        ArrayList<String> links = new ArrayList<String>();

        if (StringUtils.isNoneBlank(html)) { 
            Document doc = Jsoup.parse(html);
            if (StringUtils.isNoneBlank(listEl)) {         
                Elements contents = doc.select(listEl);
                if (contents != null) {
                    for(Element e : contents) {
                        Elements linkHref = e.select("a");
                        String link = linkHref.attr("href");
                        if (StringUtils.isNoneBlank(link)) {
                            /**
                             * DB에 존재하면 처리하지 않는다.
                             * 답글인경우 답글이 존재하면 처리하지 않는다.
                             */
                            if (!linkHref.attr("href").toLowerCase().contains("http")) {
                                if(StringUtils.isNoneBlank(contentsUrl)) {
                                    link = contentsUrl + link;
                                }
                                else {
                                    link = defaultUrl + link;
                                }                    
                            }
                            if (crawlerService.getCrawlerCollectionByLink(link, crawler)) {
                                links.add(link);
                            }
                        }
                    }
                }
            }
        }
        return links;
    }
    
    /**
     * 상세
     * @param html
     * @param link
     */
    public HashMap<String, Object> parserContents(String link, TbCrawler crawler) {

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
        String replyContentsEl  = crawler.getReplyContentsEl();
        String itemEl           = crawler.getItemEl();
        
        HashMap<String, Object> content = new HashMap<String, Object>();
        
        String title            = "";
        String contents         = "";
        String replyContents    = "";
        
        HashMap<String, String> items       = new HashMap<String, String>();
        ArrayList<String> images            = new ArrayList<String>();
        ArrayList<String>  imagesContents   = new ArrayList<String>();
        
        if (StringUtils.isNoneBlank(link)) {
            try {
                String html = crawlerClient.post(link);
                
                if (StringUtils.isNoneBlank(html)) {
                    Document doc = Jsoup.parse(html);
        //            doc.outputSettings().prettyPrint(false); // 줄바꿈 살림

                    if (StringUtils.isNoneBlank(bodyEl)) {
                        Elements body   = doc.select(bodyEl);
                        // 제목
                        if (StringUtils.isNoneBlank(titleEl)) {
                            title    = body.select(titleEl).text().toString();
                        }
                        
                        // 내용
                        if (StringUtils.isNoneBlank(contentsEl)) {
                            contents = body.select(contentsEl).html().toString();
                            
            
                            // 이미지 추출
                            Elements image = body.select(crawler.getContentsEl() + " img");
                            for(Element img : image) {
                                String imageFullPath = "";
                                String imgUrl = "";
                                if (img.attr("src").toLowerCase().contains("http")) {
                                    imgUrl = img.attr("src");
                                }
                                else {
                                    imgUrl = defaultUrl + img.attr("src");
                                }
                                
                                if (StringUtils.isNoneBlank(imgUrl)) {
                                    imageFullPath = CrawlerUtil.downloadImage(imageDownloaPath, imgUrl);
                                    images.add(imageFullPath);
                                }                
                            }
                            
                            // OCR : TEXT 추출
                            imagesContents    = extractionText(images);
                            
                        }
                        
                        // 답글
                        if (StringUtils.isNoneBlank(replyContentsEl)) {
                            replyContents = body.select(replyContentsEl).html().toString();
                        }
                        
                        // 항목 
                        if (StringUtils.isNoneBlank(itemEl)) {
                            items = parserItems(body, itemEl);
                        }
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        content.put("link", link);
        content.put("title", title);
        content.put("contents", contents);
        content.put("replyContents", replyContents);
        content.put("images", gson.toJson(images).replaceAll(imageDownloaPath, defaultUrl));
        content.put("imagesContents", gson.toJson(imagesContents));
        content.put("items", items);
        
        return content;
    }
    
    /**
     * 컨데츠 아이템
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 3. 4. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method parserItems
     *
     * @param body
     * @param itemEl
     * @return
     */
    public HashMap<String, String> parserItems(Elements body, String itemEl) {

        HashMap<String, String> items = new HashMap<String, String>();
        if (StringUtils.isNoneBlank(itemEl)) {

            Elements rows = body.select(itemEl);
            if (rows != null) {

                Element first = rows.first();
                
                if (first.getElementsByTag("tr").size() > 0) {
                    items = parserItemsTr(body, itemEl);
                }
                else if (first.getElementsByTag("ul").size() > 0) {
                    items = parserItemsUl(body, itemEl);
                }
                else {
                    log.debug("first row : {} = 없음({})" , itemEl, first);
                }
            }
        }
        return items;
    }

    /**
     * <TR></TR> 분석
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 3. 5. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method parserItemsTr
     *
     * @param body
     * @param itemEl
     * @return
     */
    public HashMap<String, String> parserItemsTr(Elements body, String itemEl) {
        
        HashMap<String, String> items = new HashMap<String, String>();
        if (StringUtils.isNoneBlank(itemEl)) {

            Elements rows = body.select(itemEl);
            
            if (rows != null) {

//                Element first = rows.first();
//                log.debug("<TR></TR> first row ; {} " , first);

                for(Element row : rows) {                
                    
                    // th td
                    if (row.getElementsByTag("th").size() > 0) {
                        int cellCount = row.children().size();
                        if (cellCount > 1) {

                            Elements thcells = row.select("th");
                            Elements tdcells = row.select("td");
                            
                            for (int i=0; i<tdcells.size(); i++){
                                String key   = thcells.get(i).text().toString();
                                String value = tdcells.get(i).text().toString();
                                items.put(key, value);
                            }
                        }
                    }
                    // td td
                    else {
                        Elements cells = row.select("td");
                        int cellCount = cells.size();
                        if (cellCount > 1 && ((cellCount % 2) == 0)) {
                            for (int i=0; i < cells.size(); i += 2) {
                                String key  = cells.get(i).text().toString();
                                String value = cells.get(i+1).text().toString();
                                items.put(key, value);
                            }
                        }
                    }
                }
            }
        }
            
        return items;
    }

    /**
     * <UL></UL> 분석
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 3. 5. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method parserItemsUl
     *
     * @param body
     * @param itemEl
     * @return
     */
    public HashMap<String, String> parserItemsUl(Elements body, String itemEl) {
        
        HashMap<String, String> items = new HashMap<String, String>();
        if (StringUtils.isNoneBlank(itemEl)) {

            Elements rows = body.select(itemEl);
            
            if (rows != null) {
                
//                Element first = rows.first();
//                log.debug("<UL></UL> first row ; {} " , first);

                for(Element row : rows) {
                    
                    // li
                    Elements cells = row.select("li");
                    for (int i=0; i < cells.size(); i++) {

                        String key = "";
                        String value = "";
                        String email = "";
                        String e = cells.get(i).text().toString();
                        String[] a = e.split(" ");
                        
                        // 이메일 존재하는제 체크 존재하면 map에 넣고 삭제함.
                        for (String s : a) {
                            if (CrawlerUtil.isValidEmail(s)) {
                                email = s;
                                break;
                            }
                        } 
                        if (StringUtils.isNoneBlank(email)) {
                            items.put("이메일", email);
                            items.put("작성자", a[0]);
                        }
                        key   = a[0];
                        value = a[1];
                        items.put(key, value);
                    }
                }
            }     
        }
        
        return items;
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
    public ArrayList<String> extractionText(ArrayList<String> images) {
        ArrayList<String> imagesContents = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        if (!images.isEmpty() && images != null && images.size() > 0) {
            for(String imageFullPath : images) {
                sb.append(CrawlerUtil.doOCR(imageFullPath, datapath));
                sb.append("\n");
                imagesContents.add(sb.toString());
            }
        }               
        return imagesContents;
    }
    public StringBuffer extractionText(String imageFullPath) {
        StringBuffer sb = new StringBuffer();
        sb.append(CrawlerUtil.doOCR(imageFullPath, datapath));
        sb.append("\n");
           
        return sb;
    }
}
