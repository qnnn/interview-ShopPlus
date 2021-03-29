package com.kimi.myshop.plus.provider.repository;


import com.kimi.myshop.plus.provider.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author 郭富城
 */
@Repository
public interface UmsUserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据ID删除多个
     * @param ids id
     * @return 返回1则成功
     */
    @Modifying
    @Query(value = "delete FROM ums_admin WHERE id in (:ids)",nativeQuery = true)
    int deleteMulti(Set<Long> ids);

    /**
     * 删除对应角色
     * @param ids id
     */
    @Modifying
    @Query(value = "delete from ums_users_roles WHERE user_id in (:ids)",nativeQuery = true)
    void deleteMultiRoleMap(Set<Long> ids);
}
