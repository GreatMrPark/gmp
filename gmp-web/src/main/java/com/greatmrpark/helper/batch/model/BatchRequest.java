package com.greatmrpark.helper.batch.model;

import lombok.Data;

/**
 * <p>
 * <pre>
 * 
 * BatchInfo.java
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
@Data
public class BatchRequest {
    String jobName;
    String jobCron;
}
