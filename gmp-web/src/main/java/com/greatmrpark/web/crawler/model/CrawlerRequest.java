package com.greatmrpark.web.crawler.model;

import com.greatmrpark.web.common.model.Paging;

import lombok.Data;

/**
 * 크롤러 정보
 * 
 * <p>
 * com.greatmrpark.helper.crawler.model
 * CrawlerInfo.java
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
@Data
public class CrawlerRequest extends Paging {
    public String crawlerName;
    public String siteName;
    public String defaultUrl;
    public String searchUrl;
    public String collection;
    public String pageName;
    public String keyword;
    public String filter;
}
