/*
 *  Copyright (c) 2020 Great Mr. Park
 *  All right reserved.
 *  This software is the confidential and proprietary information of Great Mr. Park.
 *  You shall not disclose such Confidential Information 
 *  and shall use it only in accordance with the terms of the license agreement
 *  you entered into with Great Mr. Park.
 *
 *  Revision History
 *  Author Date Description
 *  ------------------ -------------- ------------------
 *  greatmrpark 2020. 2. 28
 *
 */
package com.greatmrpark.helper.crawler.controller;

import java.time.format.DateTimeFormatter;

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
import com.greatmrpark.helper.common.model.code.ApiMessageCode;
import com.greatmrpark.helper.common.model.db.TbCrawler;
import com.greatmrpark.helper.common.model.error.ApiCheckedException;
import com.greatmrpark.helper.common.model.error.DataNotFoundException;
import com.greatmrpark.helper.common.model.response.ResponseData;
import com.greatmrpark.helper.common.model.response.ResponseResult;
import com.greatmrpark.helper.crawler.model.CrawlerRequest;
import com.greatmrpark.helper.crawler.service.CrawlerService;

import lombok.extern.slf4j.Slf4j;

/**
 * 크롤러 Controller
 *
 * <p>
 * com.greatmrpark.helper.crawler.controller
 * CrawlerController.java
 *
 * @history
 * 개정이력(Modification Information)·
 * 수정일   수정자    수정내용
 * ------------------------------------
 * 2020. 2. 28.    greatmrpark     최초작성
 *  
 * @author greatmrpark
 * @since 2020. 2. 28.
 * @version 1.0.0
 */
@CrossOrigin
@Slf4j
@Controller
@RequestMapping(value="/v1.0")
public class CrawlerController {

    private Gson gson = new GsonBuilder().create();
    private ModelMapper modelMapper = new ModelMapper();
    private ObjectMapper objectMapper = new ObjectMapper();
    private DateTimeFormatter ldtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss"); 

    @Autowired
    CrawlerService crawlerService;

    /**
     * 크롤러 > 목록
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 3. 1. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method listCrawler
     *
     * @param params
     * @return
     */
    @PostMapping("/crawler/list")
    public ResponseEntity<ResponseResult> listCrawler(@RequestBody CrawlerRequest params) {

        /**
         * 0. 기능설명
         */
        log.debug("크롤러 > 목록");

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

        Long totalCnt    = 0L;   /* 총개수 */
        
        int startNum    = 0;   /* 시작번호*/
        int page        = 0;   /* 페이지*/
        int pageSize    = 0;   /* 한페이지개수*/
        int pageCnt     = 0;   /* 페이지개수*/
        int resultCnt   = 0;   /* 결과수 **/
        
        /**
         * 3. data 처리
         */
        try {
            resultData = crawlerService.crawlerList(params);
            if(resultData!=null) {
                totalCnt    = params.totalCnt;
                resultCnt   = params.resultCnt;
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
    
    /**
     * 크롤러 > 조회
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 3. 1. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method crudCrawler
     *
     * @param params
     * @return
     */
    @PostMapping("/crawler/view")
    public ResponseEntity<ResponseResult> viewCrawler(@RequestBody CrawlerRequest params) {

        /**
         * 0. 기능설명
         */
        log.debug("크롤러 > 조회 ");

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
            resultData = crawlerService.crawlerView(params);
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

    /**
     * 크롤러 > 저장
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 3. 1. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method saveCrawler
     *
     * @param params
     * @return
     */
    @PostMapping("/crawler/save")
    public ResponseEntity<ResponseResult> saveCrawler(@RequestBody CrawlerRequest params) {

        /**
         * 0. 기능설명
         */
        log.debug("크롤러 > 저장 ");

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
            resultData = crawlerService.crawlerSave(params);
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
    
    /**
     * 크롤러 > 실행
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 3. 1. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method startCrawler
     *
     * @param params
     * @return
     */
    @PostMapping("/crawler/start")
    public ResponseEntity<ResponseResult> startCrawler(@RequestBody CrawlerRequest params) {

        /**
         * 0. 기능설명
         */
        log.debug("크롤러 > 실행 ");

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
            resultData = crawlerService.crawlerStart(params);
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
