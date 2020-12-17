package com.greatmrpark.common.model.error;

/**
 * The enum Bot common err code.
 */
public enum CommonErrCode implements ErrCodable {

	ERR_ADMIN_0000("ERR_ADMIN_0000", "%1"),
    /**
     * %1은(는) 필수 입력 항목입니다.
     */
    ERR_ADMIN_0001("ERR_ADMIN_0001", "%1은(는) 필수 입력 항목입니다."),
    /**
     * %1은(는) 존재하지 않습니다.
     */
    ERR_ADMIN_0002("ERR_ADMIN_0002", "%1이(가) 존재하지 않습니다."),
    /**
     * %1이(가) 불일치합니다.
     */
    ERR_ADMIN_0003("ERR_ADMIN_0003", "%1이(가) 불일치합니다."),
    /**
     * 데이터 베이스 에러
     */
    ERR_ADMIN_0004("ERR_ADMIN_0004", "%1 데이터 베이스 에러가 발생했습니다."),
    /**
     * 허용되지 않는 파라메터
     */
    ERR_ADMIN_0005("ERR_ADMIN_0005", "%1 필드의 %2 값은 허용되지 않는 값입니다."),
    /**
     * %1은(는) 사용하지 않습니다.
     */
    ERR_ADMIN_0006("ERR_ADMIN_0006", "%1은(는) 중복되었습니다."),
    /**
     * %1은(는) 사용하지 않습니다.
     */
    ERR_ADMIN_0007("ERR_ADMIN_0007", "%1은(는) 사용하지 않습니다."),
    /**
     * %1은(는) 1 계정만 가능합니다.
     */
    ERR_ADMIN_0008("ERR_ADMIN_0008", "%1은(는) 1 계정만 가능합니다."),
    /**
     * %1이(가) 존재 합니다.
     */
    ERR_ADMIN_0009("ERR_ADMIN_0009", "%1이(가) 존재 합니다.");

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

    CommonErrCode(String errCode, String msg) {
        this.errCode = errCode;
        this.msg = msg;
    }
    
}
