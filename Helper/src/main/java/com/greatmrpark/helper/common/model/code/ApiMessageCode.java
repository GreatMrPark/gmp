package com.greatmrpark.helper.common.model.code;

/**
 * The enum Bot commongwa err code.
 */
public enum ApiMessageCode {

    OK("정상"),
    NG("오류"),
    
    API_MSG_0001("결과가 있습니다."),
    API_MSG_0002("결과가 없습니다."),
    API_MSG_0003("등록되었습니다."),
    API_MSG_0004("수정되었습니다."),
    API_MSG_0005("삭제되었습니다."),
    API_MSG_0006("저장되었습니다."),
    API_MSG_0007("처리되었습니다."),
    API_MSG_0008("처리 대상이 없습니다."),
    API_MSG_0009("오류가 발생하였습니다.")
    ;

    private String value;

    ApiMessageCode( String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
