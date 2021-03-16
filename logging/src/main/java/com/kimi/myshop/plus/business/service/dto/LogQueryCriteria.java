package com.kimi.myshop.plus.business.service.dto;

import com.kimi.myshop.plus.commons.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;


/**
 * 日志查询类
 *
 * @author 郭富城
 */
@Data
public class LogQueryCriteria {

    @Query(blurry = "username,description,address,requestIp,method,params")
    private String blurry;

    @Query
    private String logType;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
