package com.kimi.myshop.plus.provider.repository;

import com.kimi.myshop.plus.provider.domain.Menu;
import com.kimi.myshop.plus.provider.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 郭富城
 */
@Repository
public interface UmsMenuRepository extends JpaRepository<Menu,Long>, JpaSpecificationExecutor<Menu> {

    /**
     * 获取父级id相同的菜单数据
     * @param pid
     * @return
     */
    List<Menu> findByPid(Long pid);

    /**
     * 获取父级id为空的所有菜单数据
     * @return
     */
    List<Menu> findByPidIsNull();
}
