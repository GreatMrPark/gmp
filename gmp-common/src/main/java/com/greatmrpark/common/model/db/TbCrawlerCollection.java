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
import com.greatmrpark.common.model.convert.LocalDateTimeAttributeConverter;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CRAWLER_COLLECTION_T
 *
 * <p>
 * com.greatmrpark.helper.common.model.db
 * TbCrawlerCollection.java
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
@Table(name= "CRAWLER_COLLECTION_T")
@TableGenerator(
        name = "CRAWLER_COLLECTION_T_SEQ_GENERATOR",
        table = "SEQUENCE",
        pkColumnName = "SEQ", allocationSize = 1
)
public class TbCrawlerCollection implements Serializable {
    
    /**
     * 일련번호 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CRAWLER_COLLECTION_T_SEQ_GENERATOR")
    @Column(name="CRAWLER_COLLECTION_SEQ", nullable = false, length=20)
    @Expose
    Long crawlerCollectionSeq;
    
    /**
     * 기본도메인 
     */
    @Column(name = "DEFAULT_URL", length=128)
    @Expose
    String defaultUrl;
    
    /**
     * 사이트이름 
     */
    @Column(name = "SITE_NAME", length=64)
    @Expose
    String siteName;
    
    /**
     * 페이지명 
     */
    @Column(name = "PAGE_NAME", length=64)
    @Expose
    String pageName;
    
    /**
     * 링크 
     */
    @Column(name = "LINK", length=512)
    @Expose
    String link;
    
    /**
     * 제목 
     */
    @Column(name = "TITLE", length=128)
    @Expose
    String title;
    
    /**
     * 내용 
     */
    @Column(name = "CONTENTS")
    @Expose
    String contents;
    
    /**
     * 답글 
     */
    @Column(name = "REPLY_CONTENTS")
    @Expose
    String replyContents;
    
    /**
     * 이미지 
     */
    @Column(name = "IMAGES", length=512)
    @Expose
    String images;
    
    /**
     * 이미지 내용 
     */
    @Column(name = "IMAGES_CONTENTS")
    @Expose
    String imagesContents;
    
    /**
     * 분석내용 
     */
    @Column(name = "ANALYSIS_CONTENTS")
    @Expose
    String analysisContents;
    
    /**
     * 카테고리 
     */
    @Column(name = "CATEGORY", length=32)
    @Expose
    String category;
    
    /**
     * 출처 
     */
    @Column(name = "SOURCE", length=64)
    @Expose
    String source;
    
    /**
     * 조회 
     */
    @Column(name = "VIEWS", length=8)
    @Expose
    String views;
    
    /**
     * 접수번호 
     */
    @Column(name = "REG_NO", length=16)
    @Expose
    String regNo;

    /**
     * 품목 
     */
    @Column(name = "ITEM", length=32)
    @Expose
    String item;

    /**
     * 만족도 
     */
    @Column(name = "SATISFACTION", length=8)
    @Expose
    String satisfaction;

    /**
     * 품종 
     */
    @Column(name = "KIND", length=32)
    @Expose
    String kind;

    /**
     * 해당품목 
     */
    @Column(name = "PRODUCT", length=32)
    @Expose
    String product;
    
    /**
     * 작성자 
     */
    @Column(name = "WRITER", length=32)
    @Expose
    String writer;
    
    /**
     * 이메일 
     */
    @Column(name = "EMAIL", length=128)
    @Expose
    String email;
    
    /**
     * 첨부자료 
     */
    @Column(name = "ATTACHED", length=128)
    @Expose
    String attached;
    
    /**
     * 등록일 
     */
    @Column(name = "REG_DATE", length=32)
    @Expose
    String regDate;
    
    /**
     * 답변일 
     */
    @Column(name = "REPLY_DATE", length=32)
    @Expose
    String replyDate;

    /**
     * 분석일 
     */
    @LastModifiedDate
    @Column(name="ANLS_DATE")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    @Expose
    LocalDateTime anlsDate;
}
