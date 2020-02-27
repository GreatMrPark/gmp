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

public class CrawlKca {

    static Gson gson = new GsonBuilder().create();
    static int PTIME = 1000 * 1; 
    static int LPCYCLE = 5;
    
    static String DEFAULT_URL   = "https://www.kca.go.kr";
    static String SEARCH_URL    = "https://www.kca.go.kr/search/index.do";
    static String[] COLLECTION = {"report", "board"}; // report : 보도자료, webpage : 웹페이지, board : 게시판, menu : 메뉴정도, consumer : 소비자 문제연구
    static String QUERY_STRING  = "교원";
    static String MODE   = "list";
    static String SCODE   = "";

    static ArrayList<String> PAGES = new ArrayList<String>();
    static ArrayList<HashMap<String, String>> LINKS = new ArrayList<HashMap<String, String>>();
    static ArrayList<HashMap<String, String>> CONTENS = new ArrayList<HashMap<String, String>>();
    static Integer PAGE_SIZE = 10;
        
    public static void main(String[] args) throws Exception{
                
        // 1. 가져오기전 시간 찍기
        LocalDateTime startDateTime = LocalDateTime.now();
        System.out.println(" Start Date : " + startDateTime);
        
        // 2. 가져올 HTTP 주소 세팅
        
        CrawlKca crawl = new CrawlKca();
        
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
            
            if ("report".equals(collection)) {
                SEARCH_URL = "https://www.kca.go.kr/home/sub.do?menukey=4170";
                
                url  = SEARCH_URL;
                url += "&collection=" + collection; 
                url += "&searchKeyword=" + QUERY_STRING;
                url += "&mode=" + MODE;
                url += "&scode=" + SCODE;
                
                html = crawl.post(url);
                crawl.parserList(html, collection);
            }
            else {
                SEARCH_URL    = "https://www.kca.go.kr/search/index.do?1=1";
                
                url  = SEARCH_URL;
                url += "&collection=" + collection; 
                url += "&kwd=" + QUERY_STRING;
                
                html = crawl.post(url);
                crawl.parserListBoard(html, collection);
            }

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
                        int page = (Integer.parseInt(p) - 1) * PAGE_SIZE;
                        

                        if ("report".equals(collection)) {
                            url  = SEARCH_URL;
                            url += "&collection=" + collection; 
                            url += "&searchKeyword=" + QUERY_STRING;
                            url += "&mode=" + MODE;
                            url += "&scode=" + SCODE;
                            url += "&page="+ p;
                            html = crawl.post(url);
                            crawl.parserList(html, collection);
                        }
                        else {
                            
                            url  = SEARCH_URL;
                            url += "&collection=" + collection; 
                            url += "&kwd=" + QUERY_STRING;
                            
                            html = crawl.post(url);
                            crawl.parserListBoard(html, collection);
                        }
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
                
                if ("report".contentEquals(collection)) {
                    crawl.parserReport(html, link);
                }
                else if ("board".contentEquals(collection)) {
                    crawl.parserBoard(html, link);
                }
                else if ("webpage".contentEquals(collection)) {
                    crawl.parserSearch(html, link);
                }
                else if ("menu".contentEquals(collection)) {
                    crawl.parserSearch(html, link);
                }
                else if ("consumer".contentEquals(collection)) {
                    crawl.parserSearch(html, link);
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
        Elements pagination = doc.select(".paginate a");
        
        // 페이지
        if (pagination != null) {
            for(Element e : pagination) {            
                if (!"".equals(e.select("a").text().toString())) {
                    if (isStringDouble(e.select("a").text().toString())) {
                        if (!"1".equals(e.select("a").text().toString())) {
                            PAGES.add(e.select("a").text().toString());
                        }
                    }
                }
            }
        }
    }
    
    public void parserList(String html, String collection) {

        Document doc = Jsoup.parse(html);
        Elements contents = doc.select("#cmsBoardData table");
        String token =  doc.select("#cmsBoardData input[name=hidden]").val();
                
        
        // 목록
        if (contents != null) {
            for(Element e : contents) {
                Elements linkHref = e.select("a");            
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("collection", collection);
                map.put("link", DEFAULT_URL + "/home/sub.do" + linkHref.attr("href"));
                LINKS.add(map);
            }
        }
    }

    public void parserListBoard(String html, String collection) {

        Document doc = Jsoup.parse(html);
        Elements contents = doc.select("#contents .tit");
                
        
        // 목록
        if (contents != null) {
            for(Element e : contents) {
                Elements linkHref = e.select("a");            
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("collection", collection);
                map.put("link", linkHref.attr("href"));
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
    
    public void parserReport(String html, String link) {

        System.out.println("parserReport");
        
        Document doc = Jsoup.parse(html);
        Elements contents = doc.select(".containerIn");
        System.out.println("링크 : " + link);
        System.out.println("제목 : " + contents.select(".title").text().toString());
        System.out.println("내용 : " + contents.select(".substance").html().toString());

        // 컨덴츠
        Elements rows = contents.select("tbody tr");
        for(Element row : rows) {

            int cellCount = row.childNodeSize();

            Elements thcells = row.select("th");
            Elements tdcells = row.select("td");

            if (cellCount > 1 && ((cellCount % 2) == 0)) {
                
                for (int i=0; i < thcells.size(); i++){

                    Element th = thcells.get(i);
                    Element td = tdcells.get(i);
                    System.out.println(th.text().toString() + " : " + td.text().toString());
                }
            }
        } 
        
        System.out.println("=============================================================");
    }

    public void parserBoard(String html, String link) {

        System.out.println("parserBoard");
        
        Document doc = Jsoup.parse(html);
        Elements contents = doc.select(".containerIn");
        System.out.println("링크 : " + link);
        System.out.println("제목 : " + contents.select(".title").text().toString());
        System.out.println("내용 : " + contents.select(".substance").html().toString());

        // 컨덴츠
        Elements rows = contents.select("tbody tr");
        for(Element row : rows) {

            int cellCount = row.childNodeSize();

            Elements thcells = row.select("th");
            Elements tdcells = row.select("td");

            if (cellCount > 1 && ((cellCount % 2) == 0)) {
                
                for (int i=0; i < thcells.size(); i++){

                    Element th = thcells.get(i);
                    Element td = tdcells.get(i);
                    System.out.println(th.text().toString() + " : " + td.text().toString());
                }
            }
        } 
        
        System.out.println("=============================================================");
    }
    
    public void parserSearch(String html, String link) {

        System.out.println("parserSearch");
        
        Document doc = Jsoup.parse(html);
        Elements contents = doc.select(".containerIn");
        System.out.println("링크 : " + link);
        System.out.println("제목 : " + contents.select(".title").text().toString());
        System.out.println("내용 : " + contents.select(".substance").html().toString());

        // 컨덴츠
        Elements rows = contents.select("tbody tr");
        for(Element row : rows) {

            int cellCount = row.childNodeSize();

            Elements thcells = row.select("th");
            Elements tdcells = row.select("td");

            if (cellCount > 1 && ((cellCount % 2) == 0)) {
                
                for (int i=0; i < thcells.size(); i++){

                    Element th = thcells.get(i);
                    Element td = tdcells.get(i);
                    System.out.println(th.text().toString() + " : " + td.text().toString());
                }
            }
        } 
        
        System.out.println("=============================================================");
    }
    
    /**
     * 숫자인지 확인
     * @param s
     * @return
     */
    public static boolean isStringDouble(String s) {
        
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
