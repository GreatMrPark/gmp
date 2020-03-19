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
public class Go1372CrawlerService {

    private Gson gson = new GsonBuilder().create();
    private int PTIME = 1; 
    
    private String DEFAULT_URL   = "http://www.1372.go.kr";
    private String START_COUNT   = "0";
    private String IS_TAG_SEARCH = "Y";
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
        
        String defaultUrl = crawler.getDefaultUrl();
        String searchUrl = crawler.getSearchUrl();
        String collection = crawler.getCollection();
        String keyword = crawler.getKeyword();
        
        url  = searchUrl;
        url += "&isTagSearch=" + IS_TAG_SEARCH;
        url += "&startCount="+ START_COUNT;
        url += "&collection=" + collection; 
        url += "&query=" + keyword;
        
        try {
            html = crawlerClient.post(url);
            links = parserList(html);
            pages = parserPage(html);
            if (pages != null && pages.size() > 0) {

                CrawlerUtil.sleep(PTIME);
                
                for(String p : pages) {

                    if (!"".contentEquals(p)) {
                        int page = (Integer.parseInt(p) - 1) * PAGE_SIZE;
                        String startCount =  Integer.toString(page);
                        
                        url  = searchUrl;
                        url += "&isTagSearch=" + IS_TAG_SEARCH;
                        url += "&startCount="+ startCount;
                        url += "&collection=" + collection; 
                        url += "&query=" + keyword;
                        
                        html = crawlerClient.post(url);
                        links.addAll(parserList(html));
                    }
                }
            }            

            if (!links.isEmpty() && links != null && links.size() > 0) {
                for(String link: links) {
                    CrawlerUtil.sleep(PTIME);

                    if ("altNews".equals(collection)) {
                        contents.add(parserAltNews(link));
                    }
                    else if ("counsel".equals(collection)) {
                        contents.add(parserCounsel(link));
                    }
                    else if ("infoData".equals(collection)) {
                        contents.add(parserInfoData(link));
                    }
                    else {
                        contents.add(parserAltNews(link));
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        return links;
    }
    
    /**
     * 소비자상담센터 > 알림뉴스
     * @param html
     * @param link
     */
    public HashMap<String, Object> parserAltNews(String link) {

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
                if (cellCount > 1 && ((cellCount % 2) == 0)) {
                    for (int i=0; i < cells.size(); i += 2) {
                        Element th = cells.get(i);
                        Element td = cells.get(i+1);
                        etcMap.put(th.text().toString(), td.text().toString());
                    }
                }
            }
            
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
            }
            
            // OCR
            StringBuffer sb = new StringBuffer();
            if (!imgList.isEmpty() && imgList != null && imgList.size() > 0) {
                for(String imageFullPath : imgList) {
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
        
        return content;
    }
    
    /**
     * 소비자상담센터 > 상담조회
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 3. 2. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method parserCounsel
     *
     * @param html
     * @param link
     */
    public HashMap<String, Object> parserCounsel(String link) {

        HashMap<String, Object> content = new HashMap<String, Object>();
        try {
            String html = crawlerClient.post(link);
            Document doc = Jsoup.parse(html);
            Elements contents = doc.select(".boardView");
            String title    = contents.select("#contentsViewTitle").text().toString();
            String text     = contents.select(".autocounsel_last_box").get(0).html().toString();
            String reply    = contents.select(".autocounsel_last_box").get(1).html().toString();

            // 컨덴츠
            HashMap<String, String> etcMap = new HashMap<String, String>();
            Elements rows = contents.select("tbody tr");
            for(Element row : rows) {                
                int cellCount = row.children().size();
                if (cellCount > 1) {
    
                    Elements thcells = row.select("th");
                    Elements tdcells = row.select("td");
                    
                    for (int i=0; i<tdcells.size(); i++){
                        Element th = thcells.get(i);
                        Element td = tdcells.get(i);
                        etcMap.put(th.text().toString(), td.text().toString());
                    }
                }
            } 

            // 이미지 추출
            Elements image = contents.select(".autocounsel_last_box img");
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
            
            // OCR
            StringBuffer sb = new StringBuffer();
            if (!imgList.isEmpty() && imgList != null && imgList.size() > 0) {
                for(String imageFullPath : imgList) {
                    sb.append(CrawlerUtil.doOCR(imageFullPath, datapath));
                    sb.append("\n");
                }
            }               
            String imagesContent    = sb.toString();

            content.put("link", link);
            content.put("title", title);
            content.put("contents", text);
            content.put("reply", reply);
            content.put("images", images);
            content.put("imagesContent", imagesContent);
            content.put("etcs", etcMap);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }
    
    /**
     * 소비자상담센터 > 정보자료
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 3. 2. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method parserInfoData
     *
     * @param link
     * @return
     */
    public HashMap<String, Object> parserInfoData(String link) {

        HashMap<String, Object> content = new HashMap<String, Object>();
        try {
            String html = crawlerClient.post(link);
            Document doc = Jsoup.parse(html);
            Elements contents = doc.select(".boardView");
            String title    = contents.select("#contentsViewTitle").text().toString();
            String text     = contents.select("#contentsViewTitle2").html().toString();
    
            // 컨덴츠
            HashMap<String, String> etcMap = new HashMap<String, String>();
            Elements rows = contents.select("tbody tr");
            for(Element row : rows) {
    
                Elements cells = row.select("td");
                cells.select("#contentsViewTitle2").remove();
                int cellCount = cells.size();
    
                if (((cellCount % 2) == 0) && (cellCount <=4)) {
                    
                    for (int i=0; i < cells.size(); i += 2) {
                        Element th = cells.get(i);
                        Element td = cells.get(i+1);
                        
                        etcMap.put(th.text().toString(), td.text().toString());
                    }
                }
            } 

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
            }

            // OCR
            StringBuffer sb = new StringBuffer();
            if (!imgList.isEmpty() && imgList != null && imgList.size() > 0) {
                for(String imageFullPath : imgList) {
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
        return content;
    }  
}
