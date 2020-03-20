package com.greatmrpark.helper.crawler.controller;

import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greatmrpark.common.model.code.ApiMessageCode;
import com.greatmrpark.common.model.error.ApiCheckedException;
import com.greatmrpark.common.model.error.DataNotFoundException;
import com.greatmrpark.common.model.response.ResponseData;
import com.greatmrpark.common.model.response.ResponseResult;
import com.greatmrpark.helper.crawler.model.CrawlerRequest;
import com.greatmrpark.helper.crawler.service.CrawlerScraping;

import lombok.extern.slf4j.Slf4j;

/**
 * 스크래핑
 *
 * <p>
 * com.greatmrpark.helper.crawler.controller
 * ScrapingController.java
 *
 * @history
 * 개정이력(Modification Information)·
 * 수정일   수정자    수정내용
 * ------------------------------------
 * 2020. 3. 8.    greatmrpark     최초작성
 *  
 * @author greatmrpark
 * @since 2020. 3. 8.
 * @version 1.0.0
 */
@CrossOrigin
@Slf4j
@Controller
@RequestMapping(value="/v1.0")
public class ScrapingController {

    private Gson gson = new GsonBuilder().create();
    private ModelMapper modelMapper = new ModelMapper();
    private ObjectMapper objectMapper = new ObjectMapper();
    private DateTimeFormatter ldtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss"); 

    @Autowired CrawlerScraping crawlerScraping;
    
    @PostMapping("/crawler/scraping")
    public ResponseEntity<ResponseResult> Scraping(@RequestBody CrawlerRequest params, HttpServletRequest request, HttpServletResponse response) {

        /**
         * 0. 기능설명
         */
        log.debug("크롤러 > 스크래핑 ");

        /**
         *  1. data 수신
         */
        log.debug("params : {}", gson.toJson(params));
        
        /**
         *  2. data 초기화 및 체크
         */
        ResponseResult responseResult = new ResponseResult();
        ResponseData responseData = new ResponseData();

        String resultCode       = ApiMessageCode.API_MSG_0001.name();
        String resultMsg        = ApiMessageCode.API_MSG_0001.getValue();
        Object resultData       = null;
        
        int startNum    = 0;   /* 시작번호*/
        int totalCnt    = 0;   /* 총개수 */
        int page        = 0;   /* 페이지*/
        int pageSize    = 0;   /* 한페이지개수*/
        int pageCnt     = 0;   /* 페이지개수*/
        int resultCnt   = 0;   /* 결과수 **/
        
        /**
         * 3. data 처리
         */
        try {            
            resultData = crawlerScraping.scraping(params, request, response);
            
            if(resultData!=null) {
                totalCnt    = 1;
                resultCnt   = 1;
                pageSize     = 10;
                pageCnt     = 1;
                page        = 1;
            }
            else {
                resultCode = ApiMessageCode.API_MSG_0002.name();
                resultMsg  = ApiMessageCode.API_MSG_0002.getValue();
            }
            responseResult.setOK();
        } catch (DataNotFoundException ex) {
            resultCode = ApiMessageCode.API_MSG_0008.name();
            resultMsg = ex.getMessage();
            responseResult.setOK();
        } catch (ApiCheckedException ea) {
            resultCode = ApiMessageCode.API_MSG_0009.name();
            resultMsg = ea.getMessage();
            responseResult.setNG();
        } catch (Exception e) {
            resultCode = ApiMessageCode.API_MSG_0009.name();
            resultMsg = e.getMessage();
            responseResult.setNG();
            log.error("[{}] [{}] {}", this.getClass().getName() , new Object(){}.getClass().getEnclosingMethod(), e.getMessage() );
        }
        
        /**
         * 4. data 결과
         */
        responseData.setTotalCnt(totalCnt);
        responseData.setPage(page);
        responseData.setPageCnt(pageCnt);
        responseData.setPageSize(pageSize);
        
        responseData.setResultCnt(resultCnt);
        responseData.setResultData(resultData);

        responseResult.setData(responseData);
        responseResult.setResultMsg(resultMsg);
                
        return new ResponseEntity<ResponseResult>(responseResult, HttpStatus.OK);
    }
}
