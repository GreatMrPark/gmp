package com.greatmrpark.web.calendar.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greatmrpark.common.model.error.BizCheckedException;
import com.greatmrpark.common.model.error.CommonErrCode;
import com.greatmrpark.web.common.model.db.TbCalendar;
import com.greatmrpark.web.common.model.dto.RequestDTO;
import com.greatmrpark.web.common.model.dto.ResponseDTO;
import com.greatmrpark.web.common.repository.CalendarRepository;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
@Service
public class CalendarService {

    private Gson gson = new GsonBuilder().create();
    private ObjectMapper objectMapper = new ObjectMapper();
    private DateTimeFormatter ldtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
    
    @Autowired private CalendarRepository calendarRepository;

    /**
     * 목록
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 11. 27. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method list
     *
     * @param page
     * @param pageSize
     * @param searchText
     * @param startDate
     * @param endDate
     * @return
     */
    public PageInfo list(RequestDTO requestDTO, ResponseDTO responseDTO) {

        int page            = requestDTO.getPage();
        int pageSize        = requestDTO.getSize();
        String sort         = requestDTO.getSort();

        PageHelper.startPage(page, pageSize);
        List<TbCalendar> list = calendarRepository.findAll();
        PageInfo pageInfo = new PageInfo(list);
        
        return pageInfo;
    }

    /**
     * 조회
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 11. 30. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method view
     *
     * @param solarBoard
     * @return
     */
    public ResponseDTO view(RequestDTO requestDTO, ResponseDTO responseDTO) throws BizCheckedException {

        TbCalendar calendar = requestDTO.getCalendar();
        if ( calendar == null ) {
            throw new BizCheckedException(CommonErrCode.ERR_ADMIN_0001, "TbCalendar");
        }
        if ( ( calendar.getId() == null || calendar.getId() == 0) ) {
            throw new BizCheckedException(CommonErrCode.ERR_ADMIN_0001, "id");
        }

        Optional<TbCalendar> view = calendarRepository.findOptById(calendar.getId());
        log.info("view : {}", gson.toJson(view));
        if ( view.isPresent() == false) {
            log.debug("no data found({})", requestDTO.toString());
            return responseDTO.noDataFound();
        }
        
        return responseDTO.setResultInfo(view.get());
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
     * @Method add
     *
     * @param requestDTO
     * @param responseDTO
     * @return
     * @throws BizCheckedException
     */
    public ResponseDTO save(RequestDTO requestDTO, ResponseDTO responseDTO) throws BizCheckedException {

        TbCalendar calendar = requestDTO.getCalendar();
        
        if ( calendar == null ) {
            throw new BizCheckedException(CommonErrCode.ERR_ADMIN_0001, "TbCalendar");
        }

        if ( ( calendar.getId() != null || calendar.getId() == 0) ) {

            Optional<TbCalendar> calendarOpt = calendarRepository.findOptById(calendar.getId());
            if ( calendarOpt.isPresent() == true) {
                calendar = calendarOpt.get();
            }
        }
        
        calendarRepository.saveAndFlush(calendar);
        
        return responseDTO.setResultInfo(calendar);
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
     * @throws BizCheckedException
     */
    public ResponseDTO del(RequestDTO requestDTO, ResponseDTO responseDTO) throws BizCheckedException {

        TbCalendar calendar = requestDTO.getCalendar();
        
        if ( calendar == null ) {
            throw new BizCheckedException(CommonErrCode.ERR_ADMIN_0001, "TbCalendar");
        }
        if ( ( calendar.getId() == null || calendar.getId() == 0) ) {
            throw new BizCheckedException(CommonErrCode.ERR_ADMIN_0001, "id");
        }

        calendarRepository.delete(calendar);
        return responseDTO.success();
    }
}
