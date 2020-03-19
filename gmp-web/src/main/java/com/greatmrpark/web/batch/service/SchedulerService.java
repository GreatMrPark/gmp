package com.greatmrpark.web.batch.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greatmrpark.web.batch.model.BatchRequest;
import com.greatmrpark.web.common.model.db.TbBatchJob;
import com.greatmrpark.web.common.repository.BatchJobRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * <pre>
 * 
 * SchedulerService.java
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
@Slf4j
@Service
public class SchedulerService {

    private Gson gson = new GsonBuilder().create();
    private ModelMapper modelMapper = new ModelMapper();
    private ObjectMapper objectMapper = new ObjectMapper();
    private DateTimeFormatter ldtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss"); 

    @Autowired
    BatchJobRepository batchJobRepository;
    
    public String getBatchJobCron(String jobName, String cron) {

        Optional<TbBatchJob> tbBatchJobOpt = batchJobRepository.findFirstByJobNameOrderByBatchJobSeqDesc(jobName);
        if (tbBatchJobOpt.isPresent()==false) {
            return cron;
        }
        TbBatchJob tbBatchJob = tbBatchJobOpt.get();
        log.debug("{} tbBatchJob : {}", this.getClass(), gson.toJson(tbBatchJob));

        return tbBatchJob.getJobCron();
    }
    
    public Boolean getBatchJobOn(String jobName) {

        Optional<TbBatchJob> tbBatchJobOpt = batchJobRepository.findFirstByJobNameOrderByBatchJobSeqDesc(jobName);
        if (tbBatchJobOpt.isPresent()==false) {
            return false;
        }
        TbBatchJob tbBatchJob = tbBatchJobOpt.get();
        log.debug("{} tbBatchJob : {}", this.getClass(), gson.toJson(tbBatchJob));

        return tbBatchJob.getJobOn();
    }

    /**
     * 배치잡 > 스케쥴러 > 목록
     * <pre>
     * 
     * ------------------------------------
     * 2019. 8. 27.    greatmrpark     최초작성
     * </pre>
     *
     * @Method : batchJobSchedulerList
     *
     * @param params
     * @return
     */
    public List<TbBatchJob> batchJobSchedulerList() {

        /**
         * 0. 기능설명
         */
        log.debug("배치잡 > 스케쥴러 > 시작");
        
        /**
         *  1. data 수신
         */
        
        LocalDateTime toDate = LocalDateTime.now();
        String loginId   = "greatmrpark";
        
        List<TbBatchJob> contents = batchJobRepository.findAll();
        if (contents == null || contents.isEmpty() || contents.size() == 0) {
            return null;
        }
        log.debug("{} contents : {}", this.getClass(), gson.toJson(contents));
        
        List<TbBatchJob> results = contents;

        log.debug("{} results : {}", this.getClass(), gson.toJson(results));
        
        return results;
    }
    
    /**
     * 배치잡 > 스케쥴러 > 시작
     * <pre>
     * 
     * ------------------------------------
     * 2019. 7. 19.    greatmrpark     최초작성
     * </pre>
     *
     * @Method : batchJobSchedulerStart
     *
     * @param params
     * @return
     */
    public Boolean batchJobSchedulerStart(BatchRequest params) {

        /**
         * 0. 기능설명
         */
        log.debug("배치잡 > 스케쥴러 > 시작");
        
        /**
         *  1. data 수신
         */
        log.debug("params : {}", gson.toJson(params));
        
        LocalDateTime toDate = LocalDateTime.now();
        String loginId   = "greatmrpark";
        
        Optional<TbBatchJob> tbBatchJobOpt = batchJobRepository.findFirstByJobNameOrderByBatchJobSeqDesc(params.getJobName());
        if (tbBatchJobOpt.isPresent()==false) {
            return false;
        }
        TbBatchJob tbBatchJob = tbBatchJobOpt.get();
        log.debug("{} tbBatchJob : {}", this.getClass(), gson.toJson(tbBatchJob));
        
        tbBatchJob.setJobOn(true);
        tbBatchJob.setUpdId(loginId);
        tbBatchJob.setUpdDate(toDate);
        log.debug("{} tbBatchJob : {}", this.getClass(), gson.toJson(tbBatchJob));
        TbBatchJob tbBatchJobSave = batchJobRepository.saveAndFlush(tbBatchJob);
        if (tbBatchJobSave == null) {
            return false;
        }
        log.debug("{} tbBatchJobSave : {}", this.getClass(), gson.toJson(tbBatchJobSave));
        
        return true;
    }
    
    /**
     * 배치잡 > 스케쥴러 > 중지
     * <pre>
     * 
     * ------------------------------------
     * 2019. 7. 19.    greatmrpark     최초작성
     * </pre>
     *
     * @Method : batchJobSchedulerStop
     *
     * @param params
     * @return
     */
    public Boolean batchJobSchedulerStop(BatchRequest params) {

        /**
         * 0. 기능설명
         */
        log.debug("배치잡 > 스케쥴러 > 중지");
        
        /**
         *  1. data 수신
         */
        log.debug("params : {}", gson.toJson(params));
        
        LocalDateTime toDate = LocalDateTime.now();
        String loginId   = "greatmrpark";
        
        Optional<TbBatchJob> tbBatchJobOpt = batchJobRepository.findFirstByJobNameOrderByBatchJobSeqDesc(params.getJobName());
        if (tbBatchJobOpt.isPresent()==false) {
            return false;
        }
        TbBatchJob tbBatchJob = tbBatchJobOpt.get();
        log.debug("{} tbBatchJob : {}", this.getClass(), gson.toJson(tbBatchJob));
        
        tbBatchJob.setJobOn(false);
        tbBatchJob.setUpdId(loginId);
        tbBatchJob.setUpdDate(toDate);
        log.debug("{} tbBatchJob : {}", this.getClass(), gson.toJson(tbBatchJob));
        TbBatchJob tbBatchJobSave = batchJobRepository.saveAndFlush(tbBatchJob);
        if (tbBatchJobSave == null) {
            return false;
        }
        log.debug("{} tbBatchJobSave : {}", this.getClass(), gson.toJson(tbBatchJobSave));
        
        return true;
    }
    
    /**
     * 배치잡 > 스케쥴러 > 변경
     * <pre>
     * 
     * ------------------------------------
     * 2019. 7. 1.    greatmrpark     최초작성
     * </pre>
     *
     * @Method : batchJobSchedulerChange
     *
     * @param params
     * @return
     */
    public Boolean batchJobSchedulerChange(BatchRequest params) {

        /**
         * 0. 기능설명
         */
        log.debug("배치잡 > 스케쥴러 > 변경");
        
        /**
         *  1. data 수신
         */
        log.debug("params : {}", gson.toJson(params));
        
        LocalDateTime toDate = LocalDateTime.now();
        String loginId   = "greatmrpark";
        
        Optional<TbBatchJob> tbBatchJobOpt = batchJobRepository.findFirstByJobNameOrderByBatchJobSeqDesc(params.getJobName());
        if (tbBatchJobOpt.isPresent()==false) {
            return false;
        }
        TbBatchJob tbBatchJob = tbBatchJobOpt.get();
        log.debug("{} tbBatchJob : {}", this.getClass(), gson.toJson(tbBatchJob));
        
        tbBatchJob.setJobCron(params.getJobCron());
        tbBatchJob.setUpdId(loginId);
        tbBatchJob.setUpdDate(toDate);
        log.debug("{} tbBatchJob : {}", this.getClass(), gson.toJson(tbBatchJob));
        TbBatchJob tbBatchJobSave = batchJobRepository.saveAndFlush(tbBatchJob);
        if (tbBatchJobSave == null) {
            return false;
        }
        log.debug("{} tbBatchJobSave : {}", this.getClass(), gson.toJson(tbBatchJobSave));
        
        return true;
    }
    
    /**
     * 잡 실행 완료 시간 갱신
     * @param jobName
     * @return
     */
    public Boolean batchJobSchedulerExecute(String jobName) {

        /**
         * 0. 기능설명
         */
        log.debug("배치잡 > 스케쥴러 > 실행 완료 시간 갱신");
        
        /**
         *  1. data 수신
         */
        log.debug("jobName : {}", gson.toJson(jobName));
        
        LocalDateTime toDate = LocalDateTime.now();
        String loginId   = "greatmrpark";
        
        Optional<TbBatchJob> tbBatchJobOpt = batchJobRepository.findFirstByJobNameOrderByBatchJobSeqDesc(jobName);
        if (tbBatchJobOpt.isPresent()==false) {
            return false;
        }
        TbBatchJob tbBatchJob = tbBatchJobOpt.get();
        log.debug("{} tbBatchJob : {}", this.getClass(), gson.toJson(tbBatchJob));
        
        tbBatchJob.setJobExecuteDate(toDate);
        tbBatchJob.setUpdId(loginId);
        tbBatchJob.setUpdDate(toDate);
        log.debug("{} tbBatchJob : {}", this.getClass(), gson.toJson(tbBatchJob));
        TbBatchJob tbBatchJobSave = batchJobRepository.saveAndFlush(tbBatchJob);
        if (tbBatchJobSave == null) {
            return false;
        }
        log.debug("{} tbBatchJobSave : {}", this.getClass(), gson.toJson(tbBatchJobSave));
        
        return true;
    }
}
