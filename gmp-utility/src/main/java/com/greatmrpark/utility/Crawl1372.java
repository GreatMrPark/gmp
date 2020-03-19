package com.greatmrpark.utility;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sourceforge.tess4j.Tesseract;

public class Crawl1372 {

    static Gson gson = new GsonBuilder().create();
    static int PTIME = 1000 * 1; 
    static int LPCYCLE = 5;
    static String IMG_PATH      = "D:\\project\\file\\images\\download\\";
    static String DEFAULT_URL   = "http://www.1372.go.kr";
    static String SEARCH_URL    = "http://www.1372.go.kr/search.ccn?nMenuCode=66";
    static String START_COUNT   = "0";
    static String QUERY_STRING  = "교원";
    static String IS_TAG_SEARCH = "Y";
//    static String[] COLLECTION  = {"altNews", "counsel", "infoData"}; // ALL : 통합검색, altNews : 알림뉴스, counsel : 상담조회, infoData : 정보자료
    static String[] COLLECTION  = {"counsel"}; // ALL : 통합검색, altNews : 알림뉴스, counsel : 상담조회, infoData : 정보자료
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
        
        Crawl1372 crawl = new Crawl1372();
        
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
            url += "&query=" + QUERY_STRING;
            url += "&isTagSearch=" + IS_TAG_SEARCH;
            url += "&collection=" + collection; 
            url += "&startCount="+ START_COUNT;
            
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
                        int page = (Integer.parseInt(p) - 1) * PAGE_SIZE;
                        String startCount =  Integer.toString(page);
                        
                        url  = SEARCH_URL;
                        url += "&query=" + QUERY_STRING;
                        url += "&isTagSearch=" + IS_TAG_SEARCH;
                        url += "&collection=" + collection; 
                        url += "&startCount="+ startCount;
                        html = crawl.post(url);
                        crawl.parserList(html, collection);
                    }
                }
            }

        }
        
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
                                
                if ("altNews".contentEquals(collection)) {
                    crawl.parserAltNews(html, link);
                }
                else if ("counsel".contentEquals(collection)) {
                    crawl.parserCounsel(html, link);
                }
                else if ("infoData".contentEquals(collection)) {
                    crawl.parserInfoData(html, link);
                }
                else {
                    crawl.parserHtml(html);
                }
                
                System.out.println("=============================================================");
            }
        }

        System.out.println("totalCount : " + LINKS.size());
        
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
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build()).build();

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
                    PAGES.add(e.select("a").text().toString());
                }
            }
        }
    }
    
    public void parserList(String html, String collection) {

        Document doc = Jsoup.parse(html);
        Elements contents = doc.select(".searchContent ul li");
        
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

        // 컨덴츠
        Elements rows = contents.select("tbody tr");
        for(Element row : rows) {
            Elements cells = row.select("td");
            
            for(Element c : cells) {
                
                if (c.select(".cpbboardView") != null) {
                    System.out.println("내용 : " + c.select(".cpbboardView tbody tr td").html().toString());
                }
            }
        } 
    }
    
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
                downloadImage(img.attr("src"));
            }
            else {
                downloadImage(DEFAULT_URL + img.attr("src"));
            }
        }
    }
    
    public void parserCounsel(String html, String link) {

        System.out.println("parserCounsel");
        
        Document doc = Jsoup.parse(html);
        Elements contents = doc.select(".boardView");
        System.out.println("링크 : " + link);
        System.out.println("제목 : " + contents.select("#contentsViewTitle").text().toString());
        System.out.println("내용 : " + contents.select(".autocounsel_last_box #ctx").html().toString());
        System.out.println("답변 : " + contents.select(".autocounsel_last_box #re_ctx").html().toString());

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
    
    /**
     * 이미지 다운로드
     * @param imgUrl
     */
    public void downloadImage(String imgUrl) {
        
        System.out.println("IMG URL : " + imgUrl);
        
        try {
            URL url;
            url = new URL(imgUrl);
            String fileName = imgUrl.substring( imgUrl.lastIndexOf('/')+1, imgUrl.length() ); // 이미지 파일명 추출
            String ext = imgUrl.substring( imgUrl.lastIndexOf('.')+1, imgUrl.length() );  // 이미지 확장자 추출
            BufferedImage img = ImageIO.read(url);
            
            String fileFullPath = IMG_PATH+fileName;
            
            ImageIO.write(img, ext, new File(fileFullPath));
            
            doOCR(fileFullPath);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * OCR IMG TO TEXT
     * @param fileFullPath
     * @return
     */
    public static String doOCR(String fileFullPath) {
        
        System.out.println("fileFullPath : " + fileFullPath);
        
        String result = "";
        long totalTime = 0;
        long endTime = 0;
        long startTime = System.currentTimeMillis();
        File file = new File(fileFullPath);
        Tesseract tesseract = new Tesseract(); 
        try { 

            tesseract.setDatapath("D:/project/Tesseract-OCR/tessdata"); // ocr 경로
            tesseract.setLanguage("kor");
            
            ImageIO.scanForPlugins();
            result = tesseract.doOCR(file);

            System.out.println("==============================");
            System.out.println("Result For OCR : ");
            System.out.println("==============================");
            System.out.println(result);
            System.out.println("==============================");
            
            endTime = System.currentTimeMillis();
            totalTime = endTime - startTime;
            
            System.out.println("Total Time Taken For OCR: " + (totalTime / 1000));
            
            return result;
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = "";
            return result;
        } 
    }
}
