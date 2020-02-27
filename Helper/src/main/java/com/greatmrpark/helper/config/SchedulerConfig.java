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
 *  greatmrpark 2019. 7. 1.
 *
 */

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
 *  greatmrpark 2019. 7. 1.
 *
 */
package com.greatmrpark.helper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.greatmrpark.helper.batch.scheduler.DynamicScheduler;

/**
 * <p>
 * <pre>
 * com.gelix.gwaadmin.config
 * SchedulerConfig.java
 * 개정이력(Modification Information)·
 * 수정일   수정자    수정내용
 * ------------------------------------
 * 2019. 7. 1.    greatmrpark     최초작성
 * </pre>
 *  
 * @author greatmrpark
 * @since 2019. 7. 1.
 * @version 1.0.0
 */
@Configuration
@EnableScheduling
public class SchedulerConfig implements CommandLineRunner {
    
    @Autowired DynamicScheduler dynamicScheduler;
    
    @Override
    public void run(String... args) {
//        dynamicScheduler.startScheduler();
    }
}
