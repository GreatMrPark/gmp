package com.greatmrpark.helper.common.model.db;

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
import com.greatmrpark.helper.common.model.convert.LocalDateTimeAttributeConverter;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CRAWLER_T
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
@Table(name= "CRAWLER_T")
@TableGenerator(
        name = "CRAWLER_T_SEQ_GENERATOR",
        table = "SEQUENCE",
        pkColumnName = "SEQ", allocationSize = 1
)
public class TbCrawler {
    
    /**
     * 일련번호 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CRAWLER_T_SEQ_GENERATOR")
    @Column(name="CRAWLER_SEQ", nullable = false, length=20)
    @Expose
    Long crawlerSeq;
    
    /**
     * 크롤러이름 
     */
    @Column(name = "CRAWLER_NAME", length=32)
    @Expose
    String crawlerName;
    
    /**
     * 사이트명 
     */
    @Column(name = "SITE_NAME", length=64)
    @Expose
    String siteName;
                        
    /**
     * 기본URL 
     */
    @Column(name = "DEFAULT_URL", length=128)
    @Expose
    String defaultUrl;
                       
    /**
     * 검색URL 
     */
    @Column(name = "SEARCH_URL", length=512)
    @Expose
    String searchUrl;
                       
    /**
     * 수집대상 
     */
    @Column(name = "COLLECTION", length=16)
    @Expose
    String collection;
                        
    /**
     * 페이지명 
     */
    @Column(name = "PAGE_NAME", length=64)
    @Expose
    String pageName;
                        
    /**
     * 키워드 
     */
    @Column(name = "KEYWORD", length=64)
    @Expose
    String keyword;
                         
    /**
     * 필터 
     */
    @Column(name = "FILTER", length=64)
    @Expose
    String filter;
                          
    /**
     * 최종수집일 
     */
    @LastModifiedDate
    @Column(name="COLLECT_DATE")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Expose
    LocalDateTime collectDate;
    
    /* 등록자 */
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
                         
    /* 수정자 */
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
