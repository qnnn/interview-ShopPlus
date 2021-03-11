package com.kimi.myshop.plus.provider.service;

import com.kimi.myshop.plus.provider.api.UmsAdminLoginLogService;
import com.kimi.myshop.plus.provider.domain.UmsAdminLoginLog;
import com.kimi.myshop.plus.provider.mapper.UmsAdminLoginLogMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 后台用户登录日志表 服务实现类
 * </p>
 *
 * @author kimi
 * @since 2021-03-10
 */
@Service(version = "1.0.0")
public class UmsAdminLoginLogServiceImpl extends ServiceImpl<UmsAdminLoginLogMapper, UmsAdminLoginLog> implements UmsAdminLoginLogService {

    @Resource
    private UmsAdminLoginLogMapper umsAdminLoginLogMapper;

    @Override
    public int insert(UmsAdminLoginLog umsAdminLoginLog) {
        return umsAdminLoginLogMapper.insert(umsAdminLoginLog);
    }
}
