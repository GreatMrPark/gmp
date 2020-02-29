package com.greatmrpark.helper.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greatmrpark.helper.common.model.db.TbCrawler;

/**
 * 크롤러 정보
 *
 * <p>
 * com.greatmrpark.helper.common.repository
 * CrawlerRepository.java
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
@Repository
public interface CrawlerRepository extends JpaRepository<TbCrawler, Long> {
    
    /**
     * 크롤러 정보 조회
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 2. 29. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method findOptByCrawlerName
     *
     * @param crawlerName
     * @return
     */
    Optional<TbCrawler> findOptByCrawlerName(String crawlerName);

}
