package com.kimi.myshop.plus.provider.repository;

import com.kimi.myshop.plus.provider.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author 郭富城
 */
@Repository
public interface UmsRoleRepository extends JpaRepository<Role,Long>, JpaSpecificationExecutor<Role> {
}
