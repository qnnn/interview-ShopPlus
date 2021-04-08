package com.kimi.myshop.plus.provider.api;

import com.kimi.myshop.plus.provider.domain.Role;
import com.kimi.myshop.plus.provider.dto.AbridgedRoleDto;
import com.kimi.myshop.plus.provider.dto.RoleDto;
import com.kimi.myshop.plus.provider.dto.RoleQueryCriteria;
import com.kimi.myshop.plus.provider.dto.UserDto;
import com.kimi.myshop.plus.provider.dto.UserQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * @author 郭富城
 */
public interface UmsRoleService {

    /**
     * 查询全部
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Object selectAll(RoleQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<RoleDto> selectAll(RoleQueryCriteria criteria);

    /**
     * 通过ID查找单个
     * @param id id
     * @return 对应数据
     */
    RoleDto findById(Long id);

    /**
     * 通过用户获取角色简略信息
     * @param username 用户 名称
     * @return 该用户所包含的所有角色信息
     */
    List<RoleDto> findByUsersName(String username);

    /**
     *  创建角色
     * @param role 角色
     */
    void create(Role role);

    /**
     * 修改角色
     * @param role 角色
     */
    void update(Role role);

    /**
     * 修改角色对应菜单
     * @param role 角色信息和所包含的菜单信息
     */
    void updateMenu(Role role);

    /**
     * 删除角色
     * @param ids 角色
     */
    void delete(Set<Long> ids);
}
