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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greatmrpark.helper.common.model.db.TbCrawler;
import com.greatmrpark.helper.common.model.error.ApiCheckedException;
import com.greatmrpark.helper.common.model.error.ApiErrCode;
import com.greatmrpark.helper.common.repository.CrawlerRepository;
import com.greatmrpark.helper.common.utils.CrawlerUtil;
import com.greatmrpark.helper.crawler.client.CrawlerClient;
import com.greatmrpark.helper.crawler.model.CrawlerRequest;
import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

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
    private Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$");

    @Value("${gmp.file.images.download}")
    private String imageDownloaPath;
    
    @Value("${gmp.ocr.datapath}")
    private String datapath;

    @Value("${gmp.file.crawler.contents}")
    private String crawlerContents;
    
    @Autowired CrawlerRepository crawlerRepository;

    @Autowired CrawlerClient crawlerClient;

    public String scraping(CrawlerRequest params) throws ApiCheckedException {

        String crawlerName = params.getCrawlerName();
        String searchUrl = params.getSearchUrl();
        
        if (
                StringUtils.isNoneBlank(crawlerName) 
                &&
                StringUtils.isNoneBlank(searchUrl) 
         ) {
            throw new ApiCheckedException(ApiErrCode.API_ERR_0008, "crawlerName,searchUrl");
        }

        String html = null;
        if (!"".contentEquals(params.getSearchUrl())) {
            html = scrapingUrl(params.getSearchUrl());
        }
        else {
            html = scrapingHtml(params.getCrawlerName());
        }
        
        return html;
        
    }
    
    public String scraping(CrawlerRequest params, HttpServletRequest request, HttpServletResponse response) throws ApiCheckedException {

        String crawlerName = params.getCrawlerName();
        String searchUrl = params.getSearchUrl();
        
        if (
                StringUtils.isNoneBlank(crawlerName) 
                &&
                StringUtils.isNoneBlank(searchUrl) 
         ) {
            throw new ApiCheckedException(ApiErrCode.API_ERR_0008, "crawlerName,searchUrl");
        }

        String html = null;
        if (!"".contentEquals(params.getSearchUrl())) {
            html = scrapingUrl(params.getSearchUrl(), request, response);
        }
        else {
            html = scrapingHtml(params.getCrawlerName());
        }
        
        return html;
        
    }

    public String scrapingHtml(String crawlerName) throws ApiCheckedException {
        
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

    public String scrapingHtml(TbCrawler crawler) throws ApiCheckedException {
        
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
        
        html = scrapingUrl(url);
        
        return html;
    }

    public String scrapingUrl(String searchUrl) throws ApiCheckedException {
        
        LocalDateTime startDateTime = LocalDateTime.now();

        String html = null;
        
        try {            
            URL url = new URL(searchUrl); 
            String protocol = url.getProtocol();
            String host = url.getHost(); 
            String authority = url.getAuthority(); 
            String defaultUrl = protocol + "://" +authority;

            log.debug("url : {}" , url);
            log.debug("protocol : {}" , protocol);
            log.debug("host : {}" , host);
            log.debug("authority : {}" , authority);
            log.debug("defaultUrl : {}" , defaultUrl);
            
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36";        
//            Response jsoupResponse = Jsoup.connect(searchUrl)
//                    .userAgent(userAgent)
//                    .timeout(3000)
//                    .header("Origin", defaultUrl)
//                    .header("Referer", defaultUrl)
//                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//                    .header("Content-Type", "application/x-www-form-urlencoded; utf-8")
//                    .header("Accept-Encoding", "gzip, deflate, br")
//                    .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
//                    .method(Connection.Method.POST)
//                    .execute();
//            Document doc = jsoupResponse.parse();
              
              Document doc = Jsoup.connect(searchUrl).get();
            
//                    url.openConnection().getInputStream(),
//                    "UTF-8",
//                    defaultUrl);

              
            ArrayList<String> links = new ArrayList<String>();
            Elements elems = new Elements();
            
            // src attribute 가 있는 엘리먼트들을 선택
            elems = doc.select("[src]");
            for( Element elem : elems ){
                if( !elem.attr("src").equals(elem.attr("abs:src")) ){
                    elem.attr("src", elem.attr("abs:src"));

                    if (StringUtils.isNoneBlank(elem.attr("src"))) {
                        links.add(elem.attr("src").toString());
                    }
                }
            }
             
            // href attribute 가 있는 엘리먼트들을 선택
            elems = doc.select("[href]");
            for( Element elem : elems ){
                if( !elem.attr("href").equals(elem.attr("abs:href")) ){
                    elem.attr("href", elem.attr("abs:href"));
                    
                    if (StringUtils.isNoneBlank(elem.attr("href"))) {
                        links.add(elem.attr("href"));
                    } 
                }
            }
            
            // a target _self
            elems = doc.getElementsByTag("a");
            for( Element elem : elems ){
                if( !elem.attr("target").equals("_blank") ){
                    elem.attr("target", "_self");
                }
                if( !elem.attr("target").equals("_new") ){
                    elem.attr("target", "_self");
                }
                
            }
            
            // script 제거
//            elems = doc.getElementsByTag("script");
//            for( Element elem : elems ){
//                elem.remove();
//            }
//            scrapingDownload(links);
            log.debug("links : {}" , gson.toJson(links));
                        
            html = doc.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        LocalDateTime endDateTime = LocalDateTime.now();
        Duration duration = Duration.between(startDateTime, endDateTime);
        log.debug("수행시간 : {} Seconds({}~{})" ,duration.getSeconds(), startDateTime, endDateTime);

        return html;
    }
    
    public String scrapingUrl(String searchUrl, HttpServletRequest request, HttpServletResponse response) throws ApiCheckedException {
        
        LocalDateTime startDateTime = LocalDateTime.now();

        String html = null;
        
        try {            
            URL url = new URL(searchUrl); 
            String protocol = url.getProtocol();
            String host = url.getHost(); 
            String authority = url.getAuthority(); 
            String defaultUrl = protocol + "://" +authority;

            log.debug("url : {}" , url);
            log.debug("protocol : {}" , protocol);
            log.debug("host : {}" , host);
            log.debug("authority : {}" , authority);
            log.debug("defaultUrl : {}" , defaultUrl);
            
            String siteFilePath = scrapingDirectory(host, request, response);

            String downloadFilePath =  crawlerContents + "/";
            String webFilePath = "/gmp/crawler/contents/";
            
            downloadFilePath = downloadFilePath + siteFilePath;
            webFilePath = webFilePath + siteFilePath;
            
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36";        
            Document doc = Jsoup.connect(searchUrl).get();
              
            String link = "";
            ArrayList<String> links = new ArrayList<String>();
            Elements elems = new Elements();
            
            // src attribute 가 있는 엘리먼트들을 선택
            elems = doc.select("[src]");
            for( Element elem : elems ){
                if( !elem.attr("src").equals(elem.attr("abs:src")) ){
                    elem.attr("src", elem.attr("abs:src"));

                    if (StringUtils.isNoneBlank(elem.attr("src"))) {
                        
                        links.add(elem.attr("src").toString());
                        link = scrapingDownload(downloadFilePath, elem.attr("src").toString());
                        
                        if (!"".contentEquals(link)) {
                            elem.attr("src", scrapingWebUrlPath(webFilePath, link));
                        }
                        
                    }
                }
            }
             
            // href attribute 가 있는 엘리먼트들을 선택
            elems = doc.select("[href]");
            for( Element elem : elems ){
                if( !elem.attr("href").equals(elem.attr("abs:href")) ){
                    elem.attr("href", elem.attr("abs:href"));
                    
                    if (StringUtils.isNoneBlank(elem.attr("href"))) {
                        links.add(elem.attr("href").toString());
                        link = scrapingDownload(downloadFilePath, elem.attr("href").toString());
                        
                        if (!"".contentEquals(link)) {
                            String href = scrapingWebUrlPath(webFilePath, link);
                            elem.attr("href", href);
                            
                            log.debug("link : {}" , link);
                            if (link.contains(".css")) {
                                File file = new File(link);
                                FileReader filereader = new FileReader(file);
                                BufferedReader bufReader = new BufferedReader(filereader);
                                String line = null;
                                String css = new String();
                                while ((line = bufReader.readLine()) != null) {
                                    css += line;
                                }
                                InputSource source = new InputSource(new StringReader(css));
                                CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
                                CSSStyleSheetImpl sheet = (CSSStyleSheetImpl) parser.parseStyleSheet(source, null, null);
                                CSSRuleList ruleList = sheet.getCssRules(); 
                                Pattern URL_PATTERN = Pattern.compile("/.*?jpg|jpeg|png|gif|woff2|woff|ttf");
                                for (int i = 0; i < ruleList.getLength(); i++) { 
                                    CSSRule rule = ruleList.item(i); 
                                    if (rule instanceof CSSStyleRule) { 
                                        CSSStyleRule styleRule=(CSSStyleRule)rule; 
                                        CSSStyleDeclaration styleDeclaration = styleRule.getStyle(); 

                                        for (int j = 0; j < styleDeclaration.getLength(); j++) {
                                            String property = styleDeclaration.item(j); 
                                            String value = styleDeclaration.getPropertyCSSValue(property).getCssText();
//                                            log.debug("property : {}", property); 
//                                            log.debug("value : {}", value);
                                            
                                            Matcher m = URL_PATTERN.matcher(value);
                                            while(m.find()) {
                                                String originalUrl = m.group(0);
                                                log.debug("originalUrl : {}", originalUrl);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } 
                }
            }
            
            // a target _self
            elems = doc.getElementsByTag("a");
            for( Element elem : elems ){
                if( !elem.attr("target").equals("_blank") ){
                    elem.attr("target", "_self");
                }
                if( !elem.attr("target").equals("_new") ){
                    elem.attr("target", "_self");
                }
                
            }

//            links = scrapingDownload(downloadFilePath, links);
//            log.debug("links : {}" , gson.toJson(links));
                        
            html = doc.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        LocalDateTime endDateTime = LocalDateTime.now();
        Duration duration = Duration.between(startDateTime, endDateTime);
        log.debug("수행시간 : {} Seconds({}~{})" ,duration.getSeconds(), startDateTime, endDateTime);

        return html;
    }
    
    public String scrapingDirectory(String host, HttpServletRequest request, HttpServletResponse response) {

        log.debug("host : {}" , host);
        String formatDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String downloadFilePath =  crawlerContents + "/";
        String siteFilePath = "";
        String webFilePath = "/crawler/contents";
        
        host = host.replaceAll("\\.", "");
        siteFilePath = siteFilePath + host + "/";
        siteFilePath = siteFilePath + formatDate + "/";
        
        downloadFilePath = downloadFilePath + siteFilePath;
        webFilePath = webFilePath + siteFilePath;

        log.debug("downloadFilePath : {}" , downloadFilePath);
        
        File file = new File(downloadFilePath);
        if(!file.exists()) file.mkdirs();
        
        return siteFilePath;
        
    }
    
    public String scrapingWebUrlPath(String webFilePath, String filePath) {
        String webUrlPath = "";
        String fileName = filePath.substring( filePath.lastIndexOf('/')+1, filePath.length() );
        webUrlPath = webFilePath + fileName;
        return webUrlPath;
    }
    
    public String scrapingDownload(String downloadFilePath, String link) {

        int pos = link.lastIndexOf(".");
        String ext = link.substring( pos + 1 );

        String fileFullPath = "";
        if ("gif".contentEquals(ext)
                || "jpg".contentEquals(ext)
                || "png".contentEquals(ext)
            ) {
            fileFullPath = CrawlerUtil.downloadImage(downloadFilePath, link);
        }
        else if ("js".contentEquals(ext) 
                || "css".contentEquals(ext)
                || "woff2".contentEquals(ext)
                || "woff".contentEquals(ext)
                || "ttf".contentEquals(ext)
                ) {
            fileFullPath = CrawlerUtil.download(downloadFilePath, link);
        }
        
        log.debug("fileFullPath : {}" , fileFullPath);
        
        return fileFullPath;
    }
    
    public ArrayList<String> scrapingDownload(String downloadFilePath, ArrayList<String> links) {
        
        if (links != null && !links.isEmpty() && links.size() > 0) {
            for(String link : links) {

                int pos = link.lastIndexOf(".");
                String ext = link.substring( pos + 1 );
                
                String fileFullPath = "";
                if ( "gif".contentEquals(ext)
                        || "jpg".contentEquals(ext)
                        || "png".contentEquals(ext) 
                   ) {
                    fileFullPath = CrawlerUtil.downloadImage(downloadFilePath, link);
                    link = fileFullPath;
                }
                else if ("js".contentEquals(ext) || "css".contentEquals(ext)) {
                    fileFullPath = CrawlerUtil.download(downloadFilePath, link);
                    link = fileFullPath;
                }
            }
        }
        return links;
    }
}
