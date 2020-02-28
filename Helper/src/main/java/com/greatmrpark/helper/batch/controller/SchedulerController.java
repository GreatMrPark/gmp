package com.greatmrpark.helper.batch.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greatmrpark.helper.batch.model.BatchInfo;
import com.greatmrpark.helper.batch.provider.ContextProvider;
import com.greatmrpark.helper.batch.scheduler.DynamicAbstractScheduler;
import com.greatmrpark.helper.batch.service.SchedulerService;
import com.greatmrpark.helper.common.model.db.TbBatchJob;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * <pre>
 * 
 * SchedulerController.java
 * 개정이력(Modification Information)·
 * 수정일   수정자    수정내용
 * ------------------------------------
 * 2019. 7. 1.    greatmrpark     최초작성
 * </pre>
 *  
 * @author greatmrpark
 * @since 2019. 7. 1.
 * @version 1.0.0
 */
@CrossOrigin
@Slf4j
@Controller
@RequestMapping(value="/v1.0")
public class SchedulerController {

    private Gson gson = new GsonBuilder().create();
    private ObjectMapper objectMapper = new ObjectMapper();
    private DateTimeFormatter ldtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss"); 

    @Autowired
    SchedulerService schedulerService;

    @PostMapping("/scheduler/list")
    public ResponseEntity<List<TbBatchJob>> listScheduler() {

        /**
         * 0. 기능설명
         */
        log.debug("배치잡 > 스케쥴러 > 목록");
        
        List<TbBatchJob> result = schedulerService.batchJobSchedulerList();
          
        return new ResponseEntity<List<TbBatchJob>>(result, HttpStatus.OK);
    }
    
    /**
     * 배치잡 > 스케쥴러 > 시작
     * <pre>
     * 
     * ------------------------------------
     * 2019. 7. 1.    greatmrpark     최초작성
     * </pre>
     *
     * @Method : startScheduler
     *
     * @return
     */
    @PostMapping("/scheduler/start")
    public ResponseEntity<BatchInfo> startScheduler(@RequestBody BatchInfo params) {

        /**
         * 0. 기능설명
         */
        log.debug("배치잡 > 스케쥴러 > 시작");
        
        /**
         *  5. data 조회
         */
        
        if (schedulerService.batchJobSchedulerStart(params)) {
            DynamicAbstractScheduler scheduler = (DynamicAbstractScheduler) ContextProvider.getBean(params.getJobName());
            scheduler.startScheduler();
        }
        
        BatchInfo result = params;
        return new ResponseEntity<BatchInfo>(result, HttpStatus.OK);
    }

    /**
     * 배치잡 > 스케쥴러 > 중지
     * <pre>
     * 
     * ------------------------------------
     * 2019. 7. 1.    greatmrpark     최초작성
     * </pre>
     *
     * @Method : stopScheduler
     *
     * @return
     */
    @PostMapping("/scheduler/stop")
    public ResponseEntity<BatchInfo> stopScheduler(@RequestBody BatchInfo params) {

        /**
         * 0. 기능설명
         */
        log.debug("배치잡 > 스케쥴러 > 중지");
        
        /**
         *  1. data 수신
         */
        log.debug("params : {}", gson.toJson(params));
  
        /**
         *  5. data 조회
         */
        if (schedulerService.batchJobSchedulerStop(params)) {
            DynamicAbstractScheduler scheduler = (DynamicAbstractScheduler) ContextProvider.getBean(params.getJobName());
            scheduler.stopScheduler();
        }

        BatchInfo result = params;
        return new ResponseEntity<BatchInfo>(result, HttpStatus.OK);
    }

    /**
     * 배치잡 > 스케쥴러 > 변경
     * <pre>
     * 
     * ------------------------------------
     * 2019. 7. 1.    greatmrpark     최초작성
     * </pre>
     *
     * @Method : changeScheduler
     *
     * @param params
     * @return
     */
    @PostMapping("/scheduler/change")
    public ResponseEntity<BatchInfo> changeScheduler(@RequestBody BatchInfo params) {

      /**
       * 0. 기능설명
       */
      log.debug("배치잡 > 스케쥴러 > 변경");
      
      /**
       *  1. data 수신
       */
      log.debug("params : {}", gson.toJson(params));
   
      /**
       *  5. data 조회
   */

      if (StringUtils.isBlank(params.getJobCron())) {
          params.setJobCron("0/5 * * * * ?");
      }
      
      if (schedulerService.batchJobSchedulerChange(params)) {
      
          DynamicAbstractScheduler scheduler = (DynamicAbstractScheduler) ContextProvider.getBean(params.getJobName());
          scheduler.changeTrigger(new CronTrigger(params.getJobCron()));
      }

      BatchInfo result = params;
      return new ResponseEntity<BatchInfo>(result, HttpStatus.OK);
    }
}
