package com.kimi.myshop.plus.provider.dto;

import com.kimi.myshop.plus.commons.annotation.Query;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author 郭富城
 */
@Data
public class UserQueryCriteria implements Serializable {

    private static final long serialVersionUID = -6957166549234044013L;

    @Query
    private Long id;

    @Query(blurry = "email,username,nickName")
    private String blurry;

    @Query
    private Boolean status;


    /**
     * 在此期间内
     *
     * @author 郭富城
     * @date 2021/3/20 18:33
     * @param
     * @return
     */
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
