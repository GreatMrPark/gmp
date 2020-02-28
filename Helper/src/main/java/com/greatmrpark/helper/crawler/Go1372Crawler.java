package com.greatmrpark.helper.crawler;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.greatmrpark.helper.common.utils.CrawlerUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Go1372Crawler {

    private String DEFAULT_URL   = "http://www.1372.go.kr";

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
                links.add(linkHref.attr("href"));
            }
        }
        return links;
    }
    
    /**
     * 상세
     * @param html
     * @param link
     */
    public void parserAltNews(String html, String link) {

        System.out.println("parserAltNews");
        
        Document doc = Jsoup.parse(html);
        doc.outputSettings().prettyPrint(false); // 줄바꿈 살림
        Elements contents = doc.select(".boardView");
        System.out.println("링크 : " + link);
        System.out.println("제목 : " + contents.select("#contentsViewTitle").text().toString());
        System.out.println("내용 : " + contents.select("#contentsViewTitle2").html().toString());
        
        // 컨덴츠
        Elements rows = contents.select("tbody tr");
        for(Element row : rows) {

            int cellCount = row.childNodeSize();
            
            if (cellCount > 1 && ((cellCount % 2) == 0)) {

                Elements cells = row.select("td");
                
                for (int i=0; i < cells.size(); i += 2) {
                    Element th = cells.get(i);
                    Element td = cells.get(i+1);
                    System.out.println(th.text().toString() + " : " + td.text().toString());
                }
            }
        } 
        
        // 이미지 추출
        Elements images = contents.select("#contentsViewTitle2 img");
        for(Element img : images) {
            System.out.println("이미지 경로 : " + DEFAULT_URL + img.attr("src"));
            if (img.attr("src").toLowerCase().contains("http://")) {
                CrawlerUtil.downloadImage(img.attr("src"));
            }
            else {
                CrawlerUtil.downloadImage(DEFAULT_URL + img.attr("src"));
            }
        }
    }
    
    public void parserCounsel(String html, String link) {

        System.out.println("parserCounsel");
        
        Document doc = Jsoup.parse(html);
        Elements contents = doc.select(".boardView");
        System.out.println("링크 : " + link);
        System.out.println("제목 : " + contents.select("#contentsViewTitle").text().toString());
        System.out.println("내용 : " + contents.select(".autocounsel_last_box").get(0).html().toString());
        System.out.println("답변 : " + contents.select(".autocounsel_last_box").get(1).html().toString());

        Elements rows = contents.select("tbody tr");
        for(Element row : rows) {
            
            int cellCount = row.children().size();
            
            if (cellCount > 1) {

                Elements thcells = row.select("th");
                Elements tdcells = row.select("td");
                
                for (int i=0; i<tdcells.size(); i++){
                    Element th = thcells.get(i);
                    Element td = tdcells.get(i);
                    System.out.println(th.text().toString() + " : " + td.text().toString());
                }
            }
        } 
    }
    
    public void parserInfoData(String html, String link) {

        
        System.out.println("parserInfoData");

        Document doc = Jsoup.parse(html);
        Elements contents = doc.select(".boardView");
        System.out.println("링크 : " + link);
        System.out.println("제목 : " + contents.select("#contentsViewTitle").text().toString());
        System.out.println("내용 : " + contents.select("#contentsViewTitle2").html().toString());

        // 컨덴츠
        Elements rows = contents.select("tbody tr");
        for(Element row : rows) {

            Elements cells = row.select("td");
            int cellCount = cells.size();

            if (cellCount > 1 && ((cellCount % 2) == 0)) {
                
                for (int i=0; i < cells.size(); i += 2){

                    Element th = cells.get(i);
                    Element td = cells.get(i+1);
                    System.out.println(th.text().toString() + " : " + td.text().toString());
                }
            }
        } 
    }    
}
