package com.greatmrpark.helper.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CrawlerClient {

    public String post(String url) throws Exception {
        
        // 1. DATA 수신
        System.out.println("================================");
        System.out.println("URL : " + url);
        System.out.println("================================");

        // 2. http client 생성
        HttpPost http = new HttpPost(url);
        http.addHeader("User-Agent", "Mozila/5.0");
        http.setHeader("Referer", url);

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
}
