package com.greatmrpark.helper.common.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.google.gson.annotations.Expose;

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
public class TbCrawlerCollection {
    
    /* 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CRAWLER_COLLECTION_T_SEQ_GENERATOR")
    @Column(name="BATCH_JOB_SEQ", nullable = false, length=20)
    @Expose
    Long crawlerCollectionSeq;
    
    /* 기본도메인 */
    @Column(name = "DEFAULT_URL", length=128)
    @Expose
    String defaultUrl;
    
    /* 사이트이름 */
    @Column(name = "SITE_NAME", length=64)
    @Expose
    String siteName;
    
    /* 페이지명 */
    @Column(name = "PAGE_NAME", length=64)
    @Expose
    String pageName;
    
    /* 링크 */
    @Column(name = "LINK", length=512)
    @Expose
    String link;
    
    /* 제목 */
    @Column(name = "TITLE", length=128)
    @Expose
    String title;
    
    /* 내용 */
    @Column(name = "CONTENTS")
    @Expose
    String contents;
    
    /* 이미지 */
    @Column(name = "IMAGES", length=512)
    @Expose
    String images;
    
    /* 텍스트 */
    @Column(name = "IMAGES_CONTENT")
    @Expose
    String imagesContent;
    
    /* 분석내용 */
    @Column(name = "ANALYSIS_CONTENT")
    @Expose
    String analysisContent;
    
    /* 카테고리 */
    @Column(name = "CATEGORY", length=32)
    @Expose
    String category;
    
    /* 등록일 */
    @Column(name = "REG_DATE", length=16)
    @Expose
    String regDate;
    
    /* 출처 */
    @Column(name = "SOURCE", length=64)
    @Expose
    String source;
    
    /* 조회 */
    @Column(name = "VIEWS", length=8)
    @Expose
    String views;
    
    /* 첨부자료 */
    @Column(name = "ATTACHED", length=32)
    @Expose
    String attached;

}
