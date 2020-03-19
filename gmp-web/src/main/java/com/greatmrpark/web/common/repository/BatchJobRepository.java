package com.greatmrpark.web.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greatmrpark.web.common.model.db.TbBatchJob;

/**
 * <p>
 * <pre>
 * 
 * BatchJobRepository.java
 * 개정이력(Modification Information)·
 * 수정일   수정자    수정내용
 * ------------------------------------
 * 2019. 5. 2.    greatmrpark     최초작성
 * </pre>
 *  
 * @author greatmrpark
 * @since 2019. 5. 2.
 * @version 1.0.0
 */
@Repository
public interface BatchJobRepository extends JpaRepository<TbBatchJob, Long> {

    Optional<TbBatchJob> findFirstByJobNameOrderByBatchJobSeqDesc(String jobName);
    
}
