package com.greatmrpark.common.model.db;

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

import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.gson.annotations.Expose;
import com.greatmrpark.common.model.convert.BooleanToYNConvert;
import com.greatmrpark.common.model.convert.LocalDateTimeAttributeConverter;

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
public class TbCrawler implements Serializable {
    
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
    * 컨덴츠 URL 
    */
    @Column(name = "CONTENTS_URL", length=512)
    @Expose
    String contentsUrl;
                       
    /**
     * 수집대상(소비자상담센터)
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
     * 기본키워드 
     */
    @Column(name = "KEYWORD", length=64)
    @Expose
    String keyword;
    
    /**
    * 컨덴츠메소드 (GET, POST)
    */
    @Column(name = "CONTENT_METHOD", length=8)
    @Expose
    String contentMethod;          
    
    /**
    * 컨덴츠타입 (application/x-www-form-urlencoded; charset=uft-8)
    */
    @Column(name = "CONTENT_TYPE", length=64)
    @Expose
    String contentType;          

    
    /**
    * 페이지사이즈 
    */
    @Column(name = "PAGE_SIZE")
    @Expose
    Integer pageSize;          

    
    /**
    * 페이지요소 
    */
    @Column(name = "PAGE_EL", length=64)
    @Expose
    String pageEl;          
    
    /**
     * 목록요소 
     */
    @Column(name = "LIST_EL", length=64)
    @Expose
    String listEl;
    
    /**
    * 본문요소 
    */
    @Column(name = "BODY_EL", length=64)
    @Expose
    String bodyEl;
    
    /**
    * 제목요소 
    */
    @Column(name = "TITLE_EL", length=64)
    @Expose
    String titleEl;
    
    /**
    * 내용요소 
    */
    @Column(name = "CONTENTS_EL", length=64)
    @Expose
    String contentsEl;
    
    /**
    * 답글요소 
    */
    @Column(name = "REPLY_CONTENTS_EL", length=64)
    @Expose
    String replyContentsEl;
    
    /**
    * 항목요소 
    */
    @Column(name = "ITEM_EL", length=64)
    @Expose
    String itemEl;    
    
    /**
     * 상세키워드 (',' 로 구분) 
     */
    @Column(name = "KEYWORD_DETAILS", length=64)
    @Expose
    String keywordDetails;
                         
    /**
     * 필터 
     */
    @Column(name = "FILTER", length=64)
    @Expose
    String filter;

    /**
     * 답글여부 (Y,N)
     */
    @Column(name="REPLY_YN", length=1)
    @Convert(converter = BooleanToYNConvert.class)
    @Expose
    Boolean replyYn = false;

    /**
     * 사용여부 (Y,N)
     */
    @Column(name="USE_YN", length=1)
    @Convert(converter = BooleanToYNConvert.class)
    @Expose
    Boolean useYn = true;

    /**
     * 삭제여부 (Y,N)
     */
    @Column(name="DEL_YN", length=1)
    @Convert(converter = BooleanToYNConvert.class)
    @Expose
    Boolean delYn = false;
                          
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
    @Column(name = "REG_ID", length=16)
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
                         
    /* 수정자 */
    @Column(name = "UPD_ID", length=16)
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
