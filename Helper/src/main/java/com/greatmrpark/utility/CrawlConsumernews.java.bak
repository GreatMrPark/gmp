package com.greatmrpark.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CrawlConsumernews {

    static Gson gson = new GsonBuilder().create();
    static int PTIME = 1000 * 1; 
    static int LPCYCLE = 5;
    
    static String DEFAULT_URL   = "https://www.consumernews.co.kr";
    static String SEARCH_URL    = "https://www.consumernews.co.kr/news/articleList.html";
    static String SC_AREA   = "A";
    static String VIEW_TYPE = "sm";
    static String QUERY_STRING  = "교원";
    static String[] COLLECTION = {"consumernews"}; // consumernews : 통합검색
    static ArrayList<String> PAGES = new ArrayList<String>();
    static ArrayList<HashMap<String, String>> LINKS = new ArrayList<HashMap<String, String>>();
    static ArrayList<HashMap<String, String>> CONTENS = new ArrayList<HashMap<String, String>>();
    static Integer PAGE_SIZE = 10;
    
    // http://www.1372.go.kr/board2/board.ccn?nMenuCode=46&gSiteCode=2&boardCode=217&mode=view&boardSeq=1964409&mpart=
    
    public static void main(String[] args) throws Exception{
                
        // 1. 가져오기전 시간 찍기
        LocalDateTime startDateTime = LocalDateTime.now();
        System.out.println(" Start Date : " + startDateTime);
        
        // 2. 가져올 HTTP 주소 세팅
        
        CrawlConsumernews crawl = new CrawlConsumernews();
        
        // 페이지를 조회함
        String html = null;
        String url = null;
        for(String collection : COLLECTION) {
            try {
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                System.out.println(PTIME / 1000 + " 초 휴식");
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                Thread.sleep(PTIME);
            } catch (InterruptedException e) {e.printStackTrace();}

            PAGES = new ArrayList<String>();
            
            url  = SEARCH_URL;
            url += "?collection=" + collection; 
            url += "&sc_word=" + QUERY_STRING;
            url += "&sc_area=" + SC_AREA;
            url += "&view_type="+ VIEW_TYPE;
            
            html = crawl.post(url);
            crawl.parserList(html, collection);

            crawl.parserPage(html);
            System.out.println("PAGES : " + PAGES);
            
            if (PAGES != null && PAGES.size() > 0) {
                for(String p : PAGES) {

                    try {
                        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                        System.out.println(PTIME / 1000 + " 초 휴식");
                        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                        Thread.sleep(PTIME);
                    } catch (InterruptedException e) {e.printStackTrace();}
                    
                    if (!"".contentEquals(p)) {
                        url  = SEARCH_URL;
                        url += "?collection=" + collection; 
                        url += "&sc_word=" + QUERY_STRING;
                        url += "&sc_area=" + SC_AREA;
                        url += "&view_type="+ VIEW_TYPE;
                        url += "&page="+ p;
                        html = crawl.post(url);
                        crawl.parserList(html, collection);
                    }
                }
            }

        }
        
        System.out.println("totalCount : " + LINKS.size());
        if (!LINKS.isEmpty() && LINKS != null && LINKS.size() > 0) {
            for(HashMap<String, String> linkMap : LINKS) {

                try {
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                    System.out.println(PTIME / 1000 + " 초 휴식");
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                    Thread.sleep(PTIME);
                } catch (InterruptedException e) {e.printStackTrace();}
                String link = (String) linkMap.get("link");
                String collection = (String) linkMap.get("collection");
                html = crawl.post(link);
                
                if ("consumernews".contentEquals(collection)) {
                    crawl.parserConsumernews(html, link);
                }
                else {
                    crawl.parserHtml(html);
                }
                
            }
        }
        
        // 12. 얼마나 걸렸나 찍어보자
        LocalDateTime endDateTime = LocalDateTime.now();
        System.out.println(" End Date : " + endDateTime);
        
        Duration duration = Duration.between(startDateTime, endDateTime);

        System.out.println("Seconds: " + duration.getSeconds());
    }
    
    public String post(String url) throws Exception {
        
        // 1. DATA 수신
        System.out.println("================================");
        System.out.println("URL : " + url);
        System.out.println("================================");

        // 2. http client 생성
        HttpPost http = new HttpPost(url);
        http.addHeader("User-Agent", "Mozila/5.0");
        http.setHeader("Referer", DEFAULT_URL);

        // 3. 가져오기를 실행할 클라이언트 객체 생성
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 4. 실행 및 실행 데이터를 Response 객체에 담음
        CloseableHttpResponse response = httpClient.execute(http);
     
        // 5. Response 받은 데이터 중, DOM 데이터를 가져와 Entity에 담음
        HttpEntity entity = response.getEntity();

        // 6. Charset을 알아내기 위해 DOM의 컨텐트 타입을 가져와 담고 Charset을 가져옴
        ContentType contentType = ContentType.getOrDefault(entity);
        Charset charset = contentType.getCharset();

        // 7. DOM 데이터를 한 줄씩 읽기 위해 Reader에 담음 (InputStream / Buffered 중 선택은 개인취향)
        BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));

        // 8. 가져온 DOM 데이터를 담기위한 그릇
        StringBuffer sb = new StringBuffer();

        // 9. DOM 데이터 가져오기
        String line = "";
        while((line=br.readLine()) != null){
            sb.append(line);
        } 
        
        return sb.toString();
    }

    public void parserPage(String html) {

        Document doc = Jsoup.parse(html);
        Elements pagination = doc.select(".pagination a");
        
        // 페이지
        if (pagination != null) {
            for(Element e : pagination) {          
                if (!"".equals(e.select("a").text().toString())) {
                    if (isStringDouble(e.select("a").text().toString())) {
                        PAGES.add(e.select("a").text().toString());
                    }
                }
            }
        }
    }
    
    public void parserList(String html, String collection) {

        Document doc = Jsoup.parse(html);
        Elements contents = doc.select(".list-titles");
        
        // 목록
        if (contents != null) {
            for(Element e : contents) {
                Elements linkHref = e.select("a");            
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("collection", collection);
                map.put("link", DEFAULT_URL + linkHref.attr("href"));
                LINKS.add(map);
            }
        }
    }
    
    public void parserHtml(String html) {

        Document doc = Jsoup.parse(html);
        Elements contents = doc.select(".boardView");
        System.out.println("제목 : " + contents.select("#contentsViewTitle").text().toString());

        Elements rows = contents.select("tbody tr");
        // 컨덴츠
        for(Element row : rows) {
            Elements cells = row.select("td");
            
            for(Element c : cells) {
                
                if (c.select(".cpbboardView") != null) {
                    System.out.println("내용 : " + c.select(".cpbboardView tbody tr td").html().toString());
                }
                
            }
            
            System.out.println("=============================================================");
        } 
    }
    
    public void parserConsumernews(String html, String link) {

        System.out.println("parserConsumernews");
        
        Document doc = Jsoup.parse(html);
        Elements contents = doc.select(".user-content");
        System.out.println("링크 : " + link);
        System.out.println("제목 : " + contents.select("#article-head-title").text().toString());
        System.out.println("내용 : " + contents.select("#article-view-content-div").html().toString());

        // 컨덴츠
        Elements rows = contents.select(".article-head-info ul li");
        for(Element row : rows) {
            System.out.println(row.text().toString());
        } 
        
        System.out.println("=============================================================");
    }
    
    public static boolean isStringDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
      }
}
