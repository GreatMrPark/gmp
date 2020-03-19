package com.greatmrpark.web.common.model.db;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.gson.annotations.Expose;
import com.greatmrpark.web.common.model.convert.BooleanToYNConvert;
import com.greatmrpark.web.common.model.convert.LocalDateTimeAttributeConverter;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BATCH_JOB_T                        
 * <p>
 * <pre>
 * 개정이력(Modification Information)·
 * 수정일   수정자    수정내용
 * ------------------------------------
 * 2019. 5. 10.    greatmrpark     최초작성
 * </pre>
 *  
 * @author greatmrpark
 * @since 2019. 5. 10.
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@Entity
@Table(name= "BATCH_JOB_T")
@TableGenerator(
        name = "BATCH_JOB_T_SEQ_GENERATOR",
        table = "SEQUENCE",
        pkColumnName = "SEQ", allocationSize = 1
)
public class TbBatchJob implements Serializable {
    
    /**
     * 일련번호
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "BATCH_JOB_T_SEQ_GENERATOR")
    @Column(name="BATCH_JOB_SEQ", nullable = false, length=20)
    @Expose
    Long batchJobSeq;

    /**
     * BATCH_JOB NAME
     */
    @Column(name = "JOB_NAME", length=128)
    @Expose
    String jobName;

    /**
     * BATCH_JOB CRON
     */
    @Column(name="JOB_CRON", length=32)
    @Expose
    String jobCron;

    /**
     * BATCH_JOB TYPE
     * S : 정적, D : 동적
     */
    @Column(name="JOB_TYPE", length=1)
    @Expose
    String jobType;

    /**
     * 잡 설정(Y:ON, N:OFF)
     * Y : 사용, N : 사용안함
     */
    @Column(name="JOB_ON", nullable = true, length=1)
    @Convert(converter = BooleanToYNConvert.class)
    @Expose
    Boolean jobOn = true;

    /**
     * 잡 설명
     */
    @Column(name="JOB_DESC", nullable = true, length=512)
    @Expose
    String jobDesc;
    
    /**
     * 잡 실행일
     */
    @CreatedDate
    @Column(name="JOB_EXECUTE_DATE", nullable = true)
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    @Expose
    LocalDateTime jobExecuteDate = LocalDateTime.now();
    
    /**
     * 스케쥴러
     */
    @Column(name="SCHEDULER", length=250)
    @Expose
    String scheduler;
    
    /**
     * 등록자 ID
     */
    @Column(name="REG_ID", length=40)
    @Expose
    String regId;
    
    /**
     * 등록일
     */
    @CreatedDate
    @Column(name="REG_DATE", nullable = true)
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    @Expose
    LocalDateTime regDate = LocalDateTime.now();

    /**
     * 수정자 ID
     */
    @Column(name="UPD_ID", length=40)
    @Expose
    String updId;

    /**
     * 수정일
     */
    @LastModifiedDate
    @Column(name="UPD_DATE")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    @Expose
    LocalDateTime updDate;
}
