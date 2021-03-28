package com.kimi.myshop.plus.provider.dto;

import com.kimi.myshop.plus.commons.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;


/**
 * @author 郭富城
 */
@Data
public class RoleQueryCriteria {

    @Query(blurry = "name,description")
    private String blurry;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
