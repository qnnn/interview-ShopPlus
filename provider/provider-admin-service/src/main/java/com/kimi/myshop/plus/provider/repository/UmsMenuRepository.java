package com.kimi.myshop.plus.provider.repository;

import com.kimi.myshop.plus.provider.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 郭富城
 */
@Repository
public interface UmsMenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {

    /**
     * 获取父级id相同的菜单数据
     *
     * @param pid 父级id
     * @return List<Menu>
     */
    List<Menu> findByPid(Long pid);

    /**
     * 获取父级id为空的所有菜单数据
     *
     * @return List<Menu>
     */
    List<Menu> findByPidIsNull();

    /**
     * 通过标题查找
     *
     * @param name 标题
     * @return Menu
     */
    Menu findByName(String name);

    /**
     * 通过路径查找
     *
     * @param uri 路径
     * @return Menu
     */
    Menu findByUri(String uri);

    /**
     * 删除角色的对应菜单
     *
     * @param id 菜单id
     */
    @Modifying
    @Query(value = "delete from ums_roles_menus WHERE menu_id = :id", nativeQuery = true)
    void deleteRoleMap(Long id);


    /**
     * 根据角色Id获取菜单
     * @param roleIds 角色id
     * @return 菜单
     */
    @Query(value = "SELECT m.* FROM  ums_permission m,ums_roles_menus r WHERE " +
            "m.id = r.menu_id AND r.role_id IN ?1", nativeQuery = true)
    LinkedHashSet<Menu> findByRoleIds(Set<Long> roleIds);
}
