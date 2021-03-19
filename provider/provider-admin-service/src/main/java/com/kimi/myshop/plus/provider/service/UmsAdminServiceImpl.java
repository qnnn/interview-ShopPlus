package com.kimi.myshop.plus.provider.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kimi.myshop.plus.provider.api.UmsAdminService;
import com.kimi.myshop.plus.provider.domain.UmsAdmin;
import com.kimi.myshop.plus.provider.mapper.UmsAdminMapper;
import com.kimi.myshop.plus.provider.service.fallback.UmsAdminServiceFallback;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 用户管理服务
 *
 * @param
 * @author 郭富城
 * @date 2020/11/21 12:32
 * @return
 */
@Service(version = "1.0.0",timeout = 5000)
public class UmsAdminServiceImpl implements UmsAdminService {


    @Resource
    private UmsAdminMapper umsAdminMapper;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public int insert(UmsAdmin umsAdmin) {
        // 初始化用户对象
        initUmsAdmin(umsAdmin);

        return umsAdminMapper.insert(umsAdmin);
    }

    @Override
    @SentinelResource(value = "getByUsername",fallback = "getByUsernameFallback", fallbackClass = UmsAdminServiceFallback.class)
    public UmsAdmin get(String username) {

        QueryWrapper<UmsAdmin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return umsAdminMapper.selectOne(queryWrapper);
    }

    @Override
    public UmsAdmin get(UmsAdmin umsAdmin) {
        QueryWrapper<UmsAdmin> queryWrapper = new QueryWrapper<>();
        queryWrapper.equals(umsAdmin);
        return umsAdminMapper.selectOne(queryWrapper);
    }

    @Override
    public int update(UmsAdmin umsAdmin) {
        UmsAdmin tmpUmsAdmin = get(umsAdmin.getUsername());

        tmpUmsAdmin.setEmail(umsAdmin.getEmail());
        tmpUmsAdmin.setNickName(umsAdmin.getNickName());
        tmpUmsAdmin.setNote(umsAdmin.getNote());
        tmpUmsAdmin.setStatus(umsAdmin.getStatus());


        return umsAdminMapper.updateById(tmpUmsAdmin);
    }

    @Override
    public void updateLoginTime(String username) {
        UmsAdmin umsAdmin = get(username);
        umsAdmin.setLoginTime(new Date());
        umsAdminMapper.updateById(umsAdmin);
    }

    @Override
    public int modifyIcon(String username, String path) {
        UmsAdmin umsAdmin = get(username);
        umsAdmin.setIcon(path);
        return umsAdminMapper.updateById(umsAdmin);
    }

    /**
     * 重置密码
     * @param username 用户名
     * @param password 密码
     */
    @Override
    public int modifyPassword(String username, String password) {
        UmsAdmin umsAdmin = get(username);
        umsAdmin.setPassword(bCryptPasswordEncoder.encode(password));
        return umsAdminMapper.updateById(umsAdmin);
    }

    /**
     * 初始化用户对象
     *
     * @param umsAdmin 用户信息
     * @return void
     * @author 郭富城
     * @date 2020/11/21 14:13
     */
    private void initUmsAdmin(UmsAdmin umsAdmin) {
        // 初始化创建时间
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setLoginTime(new Date());

        // 初始化状态
        if (umsAdmin.getStatus() == null) {
            umsAdmin.setStatus(0);
        }

        // 密码加密
        umsAdmin.setPassword(bCryptPasswordEncoder.encode(umsAdmin.getPassword()));
    }
}
