package com.greatmrpark.web.common.model.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

/**
 * The type Response dto.
 */
@Data
public class ResponseDTO {

    /**
     * The enum Result type.
     */
// 결과 코드 유형
    public enum ResultType {
        /**
         * Success result type.
         */
        Success("E0000", "성공하였습니다."),
        /**
         * Bad request result type.
         */
        BadRequest("E0001", "잘못된 요청입니다."),
        /**
         * No data found result type.
         */
        NoDataFound("E0002", "데이터가 없습니다."),
        /**
         * Exception result type.
         */
        Exception("E0003", "데이터를 다시 확인해주세요."),

        /**
         * Database error result type.
         */
        DatabaseError("E0004", "데이터베이스 오류입니다."),

        /**
         * Duplicate data result type.
         */
        DuplicateData("E0005", "중복된 데이터입니다."),

        Failed("E9998", "실패하였습니다."),
        /**
         * Unknown result type.
         */
        Unknown("E9999", "알 수 없는 오류입니다.");

        final private String code;
        final private String msg;

        private ResultType(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        /**
         * Gets code.
         *
         * @return the code
         */
        public String getCode() {
            return this.code;
        }

        /**
         * Gets msg.
         *
         * @return the msg
         */
        public String getMsg() {
            return this.msg;
        }
    }

    // 에러 코드
    private String resultCode;

    // 에러 메시지
    private String resultMsg;

    // 결과 맵
    private Map<String, Object> result;

    /**
     * 생성자
     */
    public ResponseDTO() {
        this.resultCode = ResultType.Unknown.getCode();
        this.resultMsg = ResultType.Unknown.getMsg();
        this.result = new HashMap<String, Object>();
    }

    /**
     * 결과 코드와 결과 메시지 설정
     *
     * @param resultType the result type
     * @param detail     the detail
     * @return result result
     */
    public ResponseDTO setResult(ResultType resultType, String detail) {
        String msg = resultType.getMsg();
        if (StringUtils.isEmpty(detail) == false) {
            msg += "[" + detail + "]";
        }
        this.setResultCode(resultType.getCode());
        this.setResultMsg(msg);
        return this;
    }

    /**
     * 결과 코드와 결과 메시지 설정 (결과 코드 별 상세 메시지)
     *
     * @param detail the detail
     * @return response dto
     */
    public ResponseDTO success(String detail) {
        return setResult(ResultType.Success, detail);
    }

    /**
     * Bad request response dto.
     *
     * @param detail the detail
     * @return the response dto
     */
    public ResponseDTO badRequest(String detail) {
        return setResult(ResultType.BadRequest, detail);
    }

    /**
     * No data found response dto.
     *
     * @param detail the detail
     * @return the response dto
     */
    public ResponseDTO noDataFound(String detail) {
        return setResult(ResultType.NoDataFound, detail);
    }

    /**
     * Exception response dto.
     *
     * @param detail the detail
     * @return the response dto
     */
    public ResponseDTO exception(String detail) {
        return setResult(ResultType.Exception, detail);
    }

    /**
     * Database error response dto.
     *
     * @param detail the detail
     * @return the response dto
     */
    public ResponseDTO databaseError(String detail) {
        return setResult(ResultType.DatabaseError, detail);
    }

    /**
     * Duplicate data response dto.
     *
     * @param detail the detail
     * @return the response dto
     */
    public ResponseDTO duplicateData(String detail) {
        return setResult(ResultType.DuplicateData, detail);
    }

    /**
     * Failed response dto.
     *
     * @param detail the detail
     * @return the response dto
     */
    public ResponseDTO failed(String detail) {
        return setResult(ResultType.Failed, detail);
    }

    /**
     * Unknown response dto.
     *
     * @param detail the detail
     * @return the response dto
     */
    public ResponseDTO unknown(String detail) {
        return setResult(ResultType.Unknown, detail);
    }

    /**
     * 결과 코드와 결과 메시지 설정 (결과 코드 별)
     *
     * @return response dto
     */
    public ResponseDTO success() {
        return success(null);
    }

    /**
     * Bad request response dto.
     *
     * @return the response dto
     */
    public ResponseDTO badRequest() {
        return badRequest(null);
    }

    /**
     * No data found response dto.
     *
     * @return the response dto
     */
    public ResponseDTO noDataFound() {
        return noDataFound(null);
    }

    /**
     * Exception response dto.
     *
     * @return the response dto
     */
    public ResponseDTO exception() {
        return exception(null);
    }

    /**
     * Database error response dto.
     *
     * @return the response dto
     */
    public ResponseDTO databaseError() {
        return databaseError(null);
    }

    /**
     * Duplicate data response dto.
     *
     * @return the response dto
     */
    public ResponseDTO duplicateData() {
        return duplicateData(null);
    }

    /**
     * Failed response dto.
     *
     * @return the response dto
     */
    public ResponseDTO failed() {
        return failed(null);
    }

    /**
     * Unknown response dto.
     *
     * @return the response dto
     */
    public ResponseDTO unknown() {
        return unknown(null);
    }

    /**
     * 결과 맵 저장
     *
     * @param key    the key
     * @param result the result
     */
    public void putResult(String key, Object result) {
        this.result.put(key, result);
    }

    /**
     * 결과 맵 삭제
     *
     * @param key the key
     */
    public void removeResult(String key) {
        this.result.remove(key);
    }

    /**
     * 결과 맵 저장 (이름있는 리스트)
     *
     * @param listName      the list name
     * @param list          the list
     * @param totalElements the total elements
     * @return result list
     */
    @SuppressWarnings("unchecked")
    public ResponseDTO setResultList(String listName, Object list, long totalElements) {
        if (list == null || list instanceof List == false || ((List<Object>) list).isEmpty()) {
            return this.noDataFound();
        }
        this.putResult(StringUtils.isEmpty(listName) ? "list" : listName, list);
        this.putResult(StringUtils.isEmpty(listName) ? "listTotalElements" : listName + "TotalElements", totalElements);
        return this.success();
    }
    public ResponseDTO setResultList(String listName, Object list, long totalElements, Object user) {
        if (list == null || list instanceof List == false || ((List<Object>) list).isEmpty()) {
            return this.noDataFound();
        }
        this.putResult(StringUtils.isEmpty(listName) ? "list" : listName, list);
        this.putResult(StringUtils.isEmpty(listName) ? "listTotalElements" : listName + "TotalElements", totalElements);
        this.putResult("user", user);
        return this.success();
    }

    /**
     * 결과 맵 저장 (이름 없는 리스트)
     *
     * @param list          the list
     * @param totalElements the total elements
     * @return result list
     */
    public ResponseDTO setResultList(Object list, long totalElements) {
        return setResultList(null, list, totalElements);
    }
    public ResponseDTO setResultList(Object list, long totalElements, Object user) {
        return setResultList(null, list, totalElements, user);
    }

    /**
     * 결과 맵 저장 (이름 있는 객체)
     *
     * @param infoName the info name
     * @param info     the info
     * @return result info
     */
    public ResponseDTO setResultInfo(String infoName, Object info) {
        if (info == null) {
            return this.noDataFound();
        }
        this.putResult(StringUtils.isEmpty(infoName) ? "info" : infoName, info);
        return this.success();
    }
    public ResponseDTO setResultInfo(String infoName, Object info, Object user) {
        if (info == null) {
            return this.noDataFound();
        }
        this.putResult(StringUtils.isEmpty(infoName) ? "info" : infoName, info);
        this.putResult("user", user);
        return this.success();
    }

    /**
     * 결과 맵 저장 (이름 없는 객체)
     *
     * @param info the info
     * @return result info
     */
    public ResponseDTO setResultInfo(Object info) {
        return setResultInfo(null, info);
    }
    public ResponseDTO setResultInfo(Object info, Object user) {
        return setResultInfo(null, info, user);
    }

}
