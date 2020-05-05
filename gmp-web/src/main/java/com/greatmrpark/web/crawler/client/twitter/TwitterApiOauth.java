package com.greatmrpark.web.crawler.client.twitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TwitterApiOauth {

    final static String JSSECACERTS_PATH = "/project/security/jssecacerts";
    
    public static void main(String[] args) {
        
        /**
         * javax.net.ssl.SSLHandshakeException
         */ 
        System.setProperty("javax.net.ssl.trustStore", JSSECACERTS_PATH);

        String clientId = "YOUR_CLIENT_ID"; //애플리케이션 클라이언트 아이디값"
        String clientSecret = "YOUR_CLIENT_SECRET"; //애플리케이션 클라이언트 시크릿값"

        String ContentType      = "application/x-www-form-urlencoded";
        String Authorization    =  "Basic QW5kcm9pZGt0Q2xpcDIuME1vYmlsZTo=";
        
        String userName = "USERNAME";
        String pwd      = "PASSWORD"; 

        log.debug("userName : {}",userName);

        RestTemplate restTemplate = new RestTemplate();
        String param = String.format("grant_type=password&username=%s&password=%s", userName,pwd);
        log.debug("param : {}",param);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", ContentType);
        headers.add("Authorization", Authorization);
        log.debug("headers : {}",headers);
        
        HttpEntity<String> entity = new HttpEntity<>(param, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange("https://api.twitter.com/1.1/search/tweets.json", HttpMethod.POST, entity, Map.class);

        log.debug("responseEntity.getStatusCode() : {}", responseEntity.getStatusCode().toString());
        log.debug("responseEntity..getBody() : {}", responseEntity.getBody().toString());

        Object data     = null;    

        if ("200".equals(responseEntity.getStatusCode().toString())) {
            data    = responseEntity.getBody(); 
        } else {
            log.debug("{} {} {}", "TWITTER TOKEN", "RESP_ERROR", responseEntity);
        }
  
        log.debug("RestTemplateRequest ResponseVO : {}", data);
        
        
        
        String text = null;
        try {
            text = URLEncoder.encode("그린팩토리", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/blog?query=" + text;    // json 결과
        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL,requestHeaders);

        System.out.println(responseBody);
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}
