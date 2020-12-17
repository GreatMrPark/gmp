package com.greatmrpark.web.common.model.dto;

import com.greatmrpark.web.common.model.db.TbCalendar;

import lombok.Data;

@Data
public class RequestDTO {
	
	// 페이징 
	int start = 0;
	int end = 10;
	int page = 0;
	int size = 10;
	String sort = "";
	public RequestDTO setPageable(int page, int size, String sort) {
		this.page = page;
		this.size = size;
		this.start = page * size + 1;
		this.end = page * size + size + 1;
		this.sort = sort;
		return this;
	}
	
	// 검색
	String yyyy;
	String startDate;
    String endDate;
    
    TbCalendar calendar;
}
