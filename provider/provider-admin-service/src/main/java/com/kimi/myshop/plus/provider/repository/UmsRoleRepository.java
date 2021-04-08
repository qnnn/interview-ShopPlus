package com.kimi.myshop.plus.provider.repository;

import com.kimi.myshop.plus.provider.domain.Role;
import com.kimi.myshop.plus.provider.dto.AbridgedRoleDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author 郭富城
 */
@Repository
public interface UmsRoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    /**
     * 通过角色名查找
     *
     * @param name 角色名
     * @return 对应数据
     */
    Role findByName(String name);


    /**
     * 删除包含ids所有数据
     *
     * @param ids ids
     */
    void deleteAllByIdIn(Set<Long> ids);

    /**
     * 通过用户获取角色简略信息
     *
     * @param id 用户 id
     * @return 该用户所包含的所有角色信息
     */
    @Query(value = "SELECT r.* FROM ums_role r, ums_users_roles u WHERE r.id = u.role_id AND u.user_id=?1", nativeQuery = true)
    Set<Role> findByUserId(Long id);
}
