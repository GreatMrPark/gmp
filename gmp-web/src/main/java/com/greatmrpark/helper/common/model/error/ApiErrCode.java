package com.greatmrpark.helper.common.model.error;

public enum ApiErrCode implements ErrCodable {

    /**
     * %1은(는) 필수 입력 항목입니다.
     */
    API_ERR_0001("API_ERR_0001", "%1은(는) 필수 입력 항목입니다."),
    
    /**
     * %1은(는) 존재하지 않습니다.
     */
    API_ERR_0002("API_ERR_0002", "%1이(가) 존재하지 않습니다."),
    
    /**
     * %1이(가) 불일치합니다.
     */
    API_ERR_0003("API_ERR_0003", "%1이(가) 불일치합니다."),

    /**
     * %1이(가) 발생했습니다.
     */
    API_ERR_0004("API_ERR_0004", "%1"),
    
    /**
     * %1은(는) %2만 사용 가능합니다.
     */
    API_ERR_0005("API_ERR_0005", "%1은(는) %2만 사용 가능합니다."),

    /**
     * %1을(를) 실패 하였습니다.
     */
    API_ERR_0006("API_ERR_0006", "%1을(를) 실패 하였습니다."),

    /**
     * %1이(가) 존재합니다.
     */
    API_ERR_0007("API_ERR_0007", "%1이(가) 존재합니다."),
    
    /**
     * %1 중 1개는 필수 입력 항목입니다.
     */
    API_ERR_0008("API_ERR_0008", "%1 중 1개는 필수 입력 항목입니다."),

    /**
     * 알수 없는 에러가 발생 했습니다. 관리자에게 문의하세요.
     */
    API_ERR_0099("API_ERR_0099", "%1 알수 없는 에러가 발생 했습니다.");

    private String errCode;
    private String msg;

    @Override
    public String getErrCode() {
        return this.errCode;
    }

    @Override
    public String getMessage(String... args) {
        return ErrCodeUtil.parseMessage(this.msg, args);
    }

    @Override
    public String getCode(String... args) {
        return this.errCode + " " + ErrCodeUtil.parseMessage(this.msg, args);
    }

    ApiErrCode(String errCode, String msg) {
        this.errCode = errCode;
        this.msg = msg;
    }
}
