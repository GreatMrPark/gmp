package com.greatmrpark.helper.common.model.response;

import lombok.Data;

/**
 * <p>
 * <pre>
 * 
 * Resp.java
 * 개정이력(Modification Information)·
 * 수정일   수정자    수정내용
 * ------------------------------------
 * 2019. 4. 24.    greatmrpark     최초작성
 * </pre>
 *  
 * @author greatmrpark
 * @since 2019. 4. 24.
 * @version 1.0.0
 */

@Data
public class ResponseData {
    public long totalCnt;   /* 총개수 */
    public int resultCnt;
    
    public int page;       /* 페이지*/
    public int pageCnt;    /* 페이지개수*/
    public int pageSize;    /* 한 페이지 레코드 개수*/

    public Object resultData;
}
