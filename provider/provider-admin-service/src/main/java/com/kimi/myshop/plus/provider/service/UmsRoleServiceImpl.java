package com.kimi.myshop.plus.provider.service;

import com.kimi.myshop.plus.commons.utils.PageUtil;
import com.kimi.myshop.plus.commons.utils.QueryHelp;
import com.kimi.myshop.plus.provider.api.UmsRoleService;
import com.kimi.myshop.plus.provider.api.UmsUserService;
import com.kimi.myshop.plus.provider.domain.Menu;
import com.kimi.myshop.plus.provider.domain.Role;
import com.kimi.myshop.plus.provider.domain.User;
import com.kimi.myshop.plus.provider.dto.AbridgedRoleDto;
import com.kimi.myshop.plus.provider.dto.RoleDto;
import com.kimi.myshop.plus.provider.dto.RoleQueryCriteria;
import com.kimi.myshop.plus.provider.dto.UserQueryCriteria;
import com.kimi.myshop.plus.provider.mapStruct.RoleMapper;
import com.kimi.myshop.plus.provider.repository.UmsMenuRepository;
import com.kimi.myshop.plus.provider.repository.UmsRoleRepository;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 郭富城
 */
@Service(version = "1.0.0")
public class UmsRoleServiceImpl implements UmsRoleService {

    @Resource
    UmsRoleRepository umsRoleRepository;

    @Resource
    UmsMenuRepository umsMenuRepository;

    @Resource
    UmsUserService umsUserService;

    @Resource
    RoleMapper roleMapper;


    @Override
    public Object selectAll(RoleQueryCriteria criteria, Pageable pageable) {
        // 分页查询
        Page<Role> page = umsRoleRepository.findAll(((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)), pageable);
        return PageUtil.toPage(page.map(roleMapper::toDto));
    }

    @Override
    public List<RoleDto> selectAll(RoleQueryCriteria criteria) {
        // 不分页查询
        List<Role> umsAdmins = umsRoleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return roleMapper.toDto(umsAdmins);
    }

    @Override
    public RoleDto findById(Long id) {
        Role role = umsRoleRepository.findById(id).orElseGet(Role::new);
        return roleMapper.toDto(role);
    }

    @Override
    public List<RoleDto> findByUsersName(String username) {
        User user = umsUserService.findByUsername(username);
        return roleMapper.toDto(new ArrayList<>(umsRoleRepository.findByUserId(user.getId())));
    }

    @Override
    public void create(Role role) {
        Role tmp = umsRoleRepository.findByName(role.getName());
        if (tmp != null) {
            throw new EntityExistsException("角色名已存在!");
        }
        role.setCreateTime(new Date());
        umsRoleRepository.save(role);
    }

    @Override
    public void update(Role role) {
        Role tmp = umsRoleRepository.findByName(role.getName());
        if (tmp != null && !tmp.getId().equals(role.getId())) {
            throw new EntityExistsException("角色名已存在!");
        }
        role.setCreateTime(new Date());
        umsRoleRepository.save(role);
    }

    @Override
    public void updateMenu(Role role) {
        Role result = umsRoleRepository.findById(role.getId()).orElseGet(Role::new);
        Set<Menu> storeIds = new HashSet<>();
        if (role.getMenus().size() != 0) {
            for (Menu menu : role.getMenus()) {
                findMenuIds(menu.getId(), storeIds);
                storeIds.add(menu);
            }
        }
        // 置空再赋值
        result.setMenus(new HashSet<Menu>());
        if (storeIds.size()!=0){
            result.getMenus().addAll(storeIds);
        }
        umsRoleRepository.save(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        umsRoleRepository.deleteAllByIdIn(ids);
    }


    /**
     * 通过菜单id查找其所有子菜单id
     *
     * @param id  父级菜单id
     * @param tmp 存储id容器
     */
    private void findMenuIds(Long id, Set<Menu> tmp) {
        List<Menu> sons = umsMenuRepository.findByPid(id);
        if (!sons.isEmpty()) {
            for (Menu son : sons) {
                findMenuIds(son.getId(), tmp);
            }
        }
        tmp.addAll(sons);
    }
}
