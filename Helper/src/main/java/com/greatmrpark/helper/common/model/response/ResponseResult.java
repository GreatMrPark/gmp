package com.greatmrpark.helper.common.model.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * <p>
 * <pre>
 * 
 * RespMessage.java
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
@SuppressWarnings("serial")
public class ResponseResult implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String resultCode = "";

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String resultMsg = "";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data = null;

    public void setOK() {
        this.setResultCode("OK");
    }

    public void setNG() {
        this.setResultCode("NG");
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
