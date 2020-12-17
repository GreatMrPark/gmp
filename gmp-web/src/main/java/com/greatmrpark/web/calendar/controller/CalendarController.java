package com.greatmrpark.web.calendar.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greatmrpark.common.model.error.BizCheckedException;
import com.greatmrpark.common.model.error.CommonErrCode;
import com.greatmrpark.web.calendar.service.CalendarService;
import com.greatmrpark.web.common.model.db.TbCalendar;
import com.greatmrpark.web.common.model.dto.RequestDTO;
import com.greatmrpark.web.common.model.dto.ResponseDTO;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/v1/calendar")
public class CalendarController {

    private Gson gson = new GsonBuilder().create();
    private ObjectMapper objectMapper = new ObjectMapper();
    private DateTimeFormatter ldtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 

    @Autowired private CalendarService calendarService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<?> list(@RequestParam(name = "page", defaultValue = "0", required = false) int page, 
            @RequestParam(name = "size", defaultValue = "10", required = false) int size, 
            @RequestParam(name = "sort", defaultValue = "", required = false) String sort, 
            @RequestBody RequestDTO requestDTO, 
            ResponseDTO responseDTO) {
        
        List<TbCalendar> list = new ArrayList<TbCalendar>();
        PageInfo pageInfo = calendarService.list(requestDTO.setPageable(page, size, sort), responseDTO);

        long count = pageInfo.getTotal();
        if ( count == 0 ) {
            log.debug("no data found({})", requestDTO.toString());
            responseDTO.noDataFound();
        }
        
        list = pageInfo.getList();
        if ( list == null || list.isEmpty() == true ) {
            log.debug("no data found({})", requestDTO.toString());
            responseDTO.noDataFound();
        }
        
        responseDTO.setResultList(list, count);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    /**
     * 조회
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 11. 25. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method view
     *
     * @param page
     * @param size
     * @param sort
     * @param requestDTO
     * @param responseDTO
     * @return
     */
    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public ResponseEntity<?> view(@RequestParam(name = "page", defaultValue = "0", required = false) int page, 
            @RequestParam(name = "size", defaultValue = "10", required = false) int size, 
            @RequestParam(name = "sort", defaultValue = "", required = false) String sort, 
            @RequestBody RequestDTO requestDTO, 
            ResponseDTO responseDTO) {
        try {
            calendarService.view(requestDTO.setPageable(page, size, sort), responseDTO);
        } catch (Exception e) {
            responseDTO.exception();
        }
        
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }
    
    /**
     * 저장
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 11. 30. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method save
     *
     * @param requestDTO
     * @param responseDTO
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody RequestDTO requestDTO, ResponseDTO responseDTO) {
        try {
            responseDTO = calendarService.save(requestDTO, responseDTO);
        } catch ( BizCheckedException e ) {
            responseDTO.exception(e.getMessage());
            if ( e.getCode() == CommonErrCode.ERR_ADMIN_0000.name() ||
                    e.getCode() == CommonErrCode.ERR_ADMIN_0001.name() || 
                    e.getCode() == CommonErrCode.ERR_ADMIN_0002.name() ||
                    e.getCode() == CommonErrCode.ERR_ADMIN_0003.name() ||
                    e.getCode() == CommonErrCode.ERR_ADMIN_0004.name() ||
                    e.getCode() == CommonErrCode.ERR_ADMIN_0005.name() ||
                    e.getCode() == CommonErrCode.ERR_ADMIN_0006.name() ||
                    e.getCode() == CommonErrCode.ERR_ADMIN_0007.name() || 
                    e.getCode() == CommonErrCode.ERR_ADMIN_0008.name() || 
                            e.getCode() == CommonErrCode.ERR_ADMIN_0009.name() ) {
                   responseDTO.badRequest(e.getMessage());
            }
        } catch (Exception e) {
            responseDTO.exception();
        }

        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    /**
     * 삭제
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 12. 1. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method del
     *
     * @param requestDTO
     * @param responseDTO
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public ResponseEntity<?> del(@RequestBody RequestDTO requestDTO, ResponseDTO responseDTO) {
        try {
            responseDTO = calendarService.del(requestDTO, responseDTO);
        } catch ( BizCheckedException e ) {
            responseDTO.exception(e.getMessage());
            if ( e.getCode() == CommonErrCode.ERR_ADMIN_0000.name() ||
                    e.getCode() == CommonErrCode.ERR_ADMIN_0001.name() || 
                    e.getCode() == CommonErrCode.ERR_ADMIN_0002.name() ||
                    e.getCode() == CommonErrCode.ERR_ADMIN_0003.name() ||
                    e.getCode() == CommonErrCode.ERR_ADMIN_0004.name() ||
                    e.getCode() == CommonErrCode.ERR_ADMIN_0005.name() ||
                    e.getCode() == CommonErrCode.ERR_ADMIN_0006.name() ||
                    e.getCode() == CommonErrCode.ERR_ADMIN_0007.name() || 
                            e.getCode() == CommonErrCode.ERR_ADMIN_0008.name() || 
                                    e.getCode() == CommonErrCode.ERR_ADMIN_0009.name() ) {
                   responseDTO.badRequest(e.getMessage());
            }
        } catch (Exception e) {
            responseDTO.exception();
        }

        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }
}
