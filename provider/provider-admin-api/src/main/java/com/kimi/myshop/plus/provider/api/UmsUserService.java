package com.kimi.myshop.plus.provider.api;


import com.kimi.myshop.plus.provider.dto.UserDto;
import com.kimi.myshop.plus.provider.dto.UserQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * @author 郭富城
 */
public interface UmsUserService {

    /**
     * 查询全部
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Object selectAll(UserQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<UserDto> selectAll(UserQueryCriteria criteria);
}
