package com.kimi.myshop.plus.provider.service;

import com.kimi.myshop.plus.commons.utils.FileUtil;
import com.kimi.myshop.plus.commons.utils.PageUtil;
import com.kimi.myshop.plus.commons.utils.QueryHelp;
import com.kimi.myshop.plus.provider.api.UmsUserService;
import com.kimi.myshop.plus.provider.domain.User;
import com.kimi.myshop.plus.provider.dto.AbridgedRoleDto;
import com.kimi.myshop.plus.provider.dto.UserDto;
import com.kimi.myshop.plus.provider.dto.UserQueryCriteria;
import com.kimi.myshop.plus.provider.mapStruct.UmsUserMapper;
import com.kimi.myshop.plus.provider.repository.UmsUserRepository;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author 郭富城
 */
@Service(version = "1.0.0")
public class UmsUserServiceImpl implements UmsUserService {

    @Resource
    UmsUserRepository umsUserRepository;

    @Resource
    UmsUserMapper umsUserMapper;

    @Override
    public Object selectAll(UserQueryCriteria criteria, Pageable pageable) {
        // 分页查询
        Page<User> page = umsUserRepository.findAll(((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)),pageable);
        return PageUtil.toPage(page.map(umsUserMapper::toDto));
    }

    @Override
    public UserDto findById(Long id) {
        User user = umsUserRepository.findById(id).orElseGet(User::new);
        return umsUserMapper.toDto(user);
    }

    @Override
    public List<UserDto> selectAll(UserQueryCriteria criteria) {
        // 不分页查询
        List<User> umsAdmins = umsUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        return umsUserMapper.toDto(umsAdmins);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(User user) {
        umsUserRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteMulti(Set<Long> ids) {
        umsUserRepository.deleteMultiRoleMap(ids);
        return umsUserRepository.deleteMulti(ids);
    }

    @Override
    public User findByUsername(String username) {
        return umsUserRepository.findByUsername(username);
    }


}
