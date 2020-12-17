package com.greatmrpark.common.model.db;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.gson.annotations.Expose;
import com.greatmrpark.common.model.convert.LocalDateTimeAttributeConverter;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CALENDAR
 *
 * <p>
 * com.greatmrpark.helper.common.model.db
 * TbCrawler.java
 *
 * @history
 * 개정이력(Modification Information)·
 * 수정일   수정자    수정내용
 * ------------------------------------
 * 2020. 2. 29.    greatmrpark     최초작성
 *  
 * @author greatmrpark
 * @since 2020. 2. 29.
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@Entity
@Table(name= "CALENDAR")
@TableGenerator(
        name = "CALENDAR_SEQ_GENERATOR",
        table = "SEQUENCE",
        pkColumnName = "SEQ", allocationSize = 1
)
public class TbCalendar implements Serializable {
    
    /**
     * 일련번호 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CALENDAR_SEQ_GENERATOR")
    @Column(name="ID", nullable = false, length=20)
    @Expose
    Long id;
    
    /**
     * 연도 
     */
    @Column(name = "YYYY", length=4)
    @Expose
    String yyyy;

    /**
     * 시작일 
     */
    @LastModifiedDate
    @Column(name="START_DATE")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Expose
    LocalDate startDate;

    /**
     * 종료일 
     */
    @LastModifiedDate
    @Column(name="END_DATE")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Expose
    LocalDateTime endDate;
     
    /**
     *  제목 
     */
    @Column(name = "TITLE", length=100)
    @Expose
    String title;
    
    /**
     *  내용 
     */
    @Column(name = "CONTENTS")
    @Expose
    String contents;
    
    /**
     *  위치
     */
    @Column(name = "LOCATION", length=50)
    @Expose
    String location;
        
    /**
     * 등록자 
     */
    @Column(name = "REG_NAME", length=50)
    @Expose
    String regName;
    
    /**
     * 등록자 
     */
    @Column(name = "REG_ID", length=50)
    @Expose
    String regId;
    
    /**
     * 등록일 
     */
    @LastModifiedDate
    @Column(name="REG_DATE")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Expose
    LocalDateTime regDate;
    
    /**
    * 수정자 
    */
    @Column(name = "UPD_NAME", length=50)
    @Expose
    String updName;

    /**
     * 수정자 
     */
    @Column(name = "UPD_ID", length=50)
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
