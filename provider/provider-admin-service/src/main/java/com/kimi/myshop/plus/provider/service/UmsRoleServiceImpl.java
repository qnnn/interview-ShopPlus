package com.kimi.myshop.plus.provider.service;

import com.kimi.myshop.plus.commons.utils.PageUtil;
import com.kimi.myshop.plus.commons.utils.QueryHelp;
import com.kimi.myshop.plus.provider.api.UmsRoleService;
import com.kimi.myshop.plus.provider.domain.Role;
import com.kimi.myshop.plus.provider.domain.User;
import com.kimi.myshop.plus.provider.dto.RoleDto;
import com.kimi.myshop.plus.provider.dto.RoleQueryCriteria;
import com.kimi.myshop.plus.provider.dto.UserQueryCriteria;
import com.kimi.myshop.plus.provider.mapStruct.RoleMapper;
import com.kimi.myshop.plus.provider.repository.UmsRoleRepository;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 郭富城
 */
@Service(version = "1.0.0")
public class UmsRoleServiceImpl implements UmsRoleService {

    @Resource
    UmsRoleRepository umsRoleRepository;

    @Resource
    RoleMapper roleMapper;


    @Override
    public Object selectAll(RoleQueryCriteria criteria, Pageable pageable) {
        // 分页查询
        Page<Role> page = umsRoleRepository.findAll(((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)),pageable);
        return PageUtil.toPage(page.map(roleMapper::toDto));
    }

    @Override
    public List<RoleDto> selectAll(RoleQueryCriteria criteria) {
        // 不分页查询
        List<Role> umsAdmins = umsRoleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        return roleMapper.toDto(umsAdmins);
    }
}
