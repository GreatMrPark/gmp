/*
 *  Copyright (c) 2019 GELIX, Inc.
 *  All right reserved.
 *  This software is the confidential and proprietary information of GELIX
 *  , Inc. You shall not disclose such Confidential Information and
 *  shall use it only in accordance with the terms of the license agreement
 *  you entered into with GELIX.
 *
 *  Revision History
 *  Author Date Description
 *  ------------------ -------------- ------------------
 *  greatmrpark 2019. 5. 2.
 *
 */
package com.greatmrpark.helper.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greatmrpark.helper.common.model.db.TbBatchJob;

/**
 * <p>
 * <pre>
 * com.gelix.commongwa.repository
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
