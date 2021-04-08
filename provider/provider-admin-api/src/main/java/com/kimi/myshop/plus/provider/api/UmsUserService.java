package com.kimi.myshop.plus.provider.api;


import com.kimi.myshop.plus.provider.domain.User;
import com.kimi.myshop.plus.provider.dto.UserDto;
import com.kimi.myshop.plus.provider.dto.UserQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;


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
     * 通过ID查找用户
     * @param id id
     * @return 用户Dto
     */
    UserDto findById(Long id);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<UserDto> selectAll(UserQueryCriteria criteria);

    /**
     * 创建或更新用户
     * @param user 用户
     */
    void save(User user);

    /**
     * 根据id删除多条数据
     * @param ids ids
     * @return 返回1表示成功
     */
    int deleteMulti(Set<Long> ids);

    /**
     * 通过用户名查找用户
     * @param username
     * @return
     */
    User findByUsername(String username);
}
