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
        String pageEl           = crawler.getPageEl();
        String listEl           = crawler.getListEl();
        String bodyEl           = crawler.getBodyEl();
        String titleEl          = crawler.getTitleEl();
        String contentsEl       = crawler.getContentsEl();
        String replyEl          = crawler.getReplyEl();
        String itemEl           = crawler.getItemEl();

        urlMap.put("keyword", keyword);
        urlMap.put("page", "1");
        url  = CrawlerUtil.messageTemplate(searchUrl, urlMap);
        
        try {
            html = crawlerClient.post(url);
            links = parserList(html, defaultUrl, listEl);
            pages = parserPage(html, defaultUrl, pageEl);
            if (pages != null && pages.size() > 0) {
                
                for(String p : pages) {

                    CrawlerUtil.sleep(PTIME);

                    if (!"".contentEquals(p)) {
                        
                        urlMap.put("keyword", keyword);
                        urlMap.put("page", p);
                        url  = CrawlerUtil.messageTemplate(searchUrl, urlMap);
                        
                        html = crawlerClient.post(url);
                        links.addAll(parserList(html, defaultUrl, listEl));
                    }
                }
            }
            
            if (!links.isEmpty() && links != null && links.size() > 0) {
                for(String link: links) {
                    
                    CrawlerUtil.sleep(PTIME);
                    
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
                String page = e.text().toString();
                if (!"".equals(page)) {
                    if (CrawlerUtil.isStringDouble(page)) {
                        pages.add(page);
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
    public ArrayList<String> parserList(String html, String defaultUrl, String listEl) {
        
        Document doc = Jsoup.parse(html);
        Elements contents = doc.select(listEl);
        
        // 목록
        ArrayList<String> links = new ArrayList<String>();
        if (contents != null) {
            for(Element e : contents) {
                Elements linkHref = e.select("a");
                String link = linkHref.attr("href");
                if (!"".equals(link)) {
                    /**
                     * TODO [2020.03.04] DB 조회 필요함.
                     * DB에 존재하면 처리하지 않는다.
                     */
                    if (!linkHref.attr("href").toLowerCase().contains("http")) {
                        link = defaultUrl + link;                    
                    }
                    links.add(link);
                }
            }
        }
        return links;
    }
    
    /**
     * 소비자가만드는신문 > 뉴스
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
        String replyEl          = crawler.getReplyEl();
        String itemEl           = crawler.getItemEl();
        
        HashMap<String, Object> content = new HashMap<String, Object>();
        try {
            String html = crawlerClient.post(link);
            
            Document doc = Jsoup.parse(html);
//            doc.outputSettings().prettyPrint(false); // 줄바꿈 살림
            Elements body   = doc.select(bodyEl);
            
            // 제목
            String title    = body.select(titleEl).text().toString();
            
            // 내용
            String contents = body.select(contentsEl).html().toString();
            
            // 항목 
            HashMap<String, String> items = parserItems(body, itemEl);
            
            // 이미지 추출
            Elements image = body.select(crawler.getContentsEl() + " img");
            ArrayList<String> images = new ArrayList<String>();
            for(Element img : image) {
                String imageFullPath = "";
                String imgUrl = "";
                if (img.attr("src").toLowerCase().contains("http")) {
                    imgUrl = img.attr("src");
                }
                else {
                    imgUrl = defaultUrl + img.attr("src");
                }
                
                if (!"".equals(imgUrl)) {
                    imageFullPath = CrawlerUtil.downloadImage(imageDownloaPath, imgUrl);
                    images.add(imageFullPath);
                }                
            }
            
            // OCR : TEXT 추출
            String imagesContent    = extractionText(images).toString();

            content.put("link", link);
            content.put("title", title);
            content.put("contents", contents);
            content.put("images", String.join(",", images).replaceAll(imageDownloaPath, defaultUrl));
            content.put("imagesContent", imagesContent);
            content.put("items", items);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
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
        Elements rows = body.select(itemEl);
        for(Element row : rows) {
            
            // tr 
            if (row.getElementsByTag("td").size() > 0) {

                // th td
                if (row.getElementsByTag("th").size() > 0) {
                    
                }
                // td td
                else {
                    Elements cells = row.select("td");
                    int cellCount = cells.size();
                    if (cellCount > 1 && ((cellCount % 2) == 0)) {
                        for (int i=0; i < cells.size(); i += 2) {
                            String key = cells.get(i).text().toString();
                            String value = cells.get(i+1).text().toString();
                            items.put(key, value);
                        }
                    }
                }
            }
            // li
            else {
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
                    if (!"".contentEquals(email)) {
                        items.put("이메일", email);
                        items.put("작성자", a[0]);
                    }
                    key   = a[0];
                    value = a[1];
                    items.put(key, value);
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
