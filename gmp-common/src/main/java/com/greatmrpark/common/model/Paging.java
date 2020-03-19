package com.greatmrpark.common.model;

public class Paging {
    
    /**
     * 총개수 
     */
    public long totalCnt;  

    /**
     * 결과개수
     */
    public int resultCnt;
    
    /**
     * 페이지개수
     */
    public int pageCnt;    
    
    /**
     * 페이지
     */
    public int page;       
    
    /**
     * 한페이지개수
     */
    public int pageSize;   
    
    /**
     *  시작번호
     */
    public int startNum;

    /**
     * 정열
     */
    public String orderField;
    public String orderBy;
}
