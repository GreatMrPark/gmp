package com.greatmrpark.helper.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greatmrpark.helper.common.model.db.TbCrawlerCollection;

/**
 * 크롤러 수집 정보
 *
 * <p>
 * com.greatmrpark.helper.common.repository
 * CrawlerCollectionRepository.java
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
public interface CrawlerCollectionRepository extends JpaRepository<TbCrawlerCollection, Long> {

}
