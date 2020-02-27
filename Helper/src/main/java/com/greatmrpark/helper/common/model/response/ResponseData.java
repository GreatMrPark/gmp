/*
 *  Copyright (c) 2019 KEPCO, Inc.
 *  All right reserved.
 *  This software is the confidential and proprietary information of KEPCO
 *  , Inc. You shall not disclose such Confidential Information and
 *  shall use it only in accordance with the terms of the license agreement
 *  you entered into with KEPCO.
 *
 *  Revision History
 *  Author Date Description
 *  ------------------ -------------- ------------------
 *  greatmrpark 2019. 4. 24.
 *
 */

/*
 *  Copyright (c) 2019 KEPCO, Inc.
 *  All right reserved.
 *  This software is the confidential and proprietary information of KEPCO
 *  , Inc. You shall not disclose such Confidential Information and
 *  shall use it only in accordance with the terms of the license agreement
 *  you entered into with KEPCO.
 *
 *  Revision History
 *  Author Date Description
 *  ------------------ -------------- ------------------
 *  greatmrpark 2019. 4. 24.
 *
 */
package com.greatmrpark.helper.common.model.response;

import lombok.Data;

/**
 * <p>
 * <pre>
 * com.gelix.gwaapi.domain.commongwa
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
    public int page;       /* 페이지*/
    public int pageCnt;    /* 페이지개수*/

    public int resultCnt;
    public Object resultData;
}
