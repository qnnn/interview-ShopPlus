package com.kimi.myshop.plus.provider.api;

import com.kimi.myshop.plus.provider.domain.Menu;
import com.kimi.myshop.plus.provider.domain.User;
import com.kimi.myshop.plus.provider.dto.MenuDto;
import com.kimi.myshop.plus.provider.dto.MenuQueryCriteria;
import com.kimi.myshop.plus.provider.dto.RoleDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * @author 郭富城
 */
public interface UmsMenuService {

    /**
     * 查询全部
     * @param criteria 条件
     * @return /
     */
    Object selectAll(MenuQueryCriteria criteria) throws IllegalAccessException;


    /**
     * 创建菜单.
     * @param menu 菜单
     */
    void create(Menu menu);

    /**
     *  更新菜单
     * @param menu 菜单
     */
    void update(Menu menu);

    /**
     * 根据id删除多条数据
     * @param ids ids
     */
    void deleteMulti(Set<Long> ids);

    /**
     *  通过id获取用户
     * @param id id
     * @return MenuDto
     */
    MenuDto findById(Long id);

    /**
     * 递归获取父级数据
     * @param menuDto 当前菜单
     * @param menus 递归载体
     * @return 逐层父级类目相同的菜单
     */
    List<MenuDto> getParent(MenuDto menuDto, List<Menu> menus);


    /**
     * 获取树
     * @param menuDtos 存放树的队列
     * @return 返回树
     */
    List<MenuDto> getTree(List<MenuDto> menuDtos);

    /**
     *  获取菜单
     * @param pid
     * @return
     */
    List<MenuDto> getMenus(Long pid);
}
