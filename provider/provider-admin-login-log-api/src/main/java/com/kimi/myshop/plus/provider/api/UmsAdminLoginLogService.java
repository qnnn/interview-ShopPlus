package com.kimi.myshop.plus.provider.api;

import com.kimi.myshop.plus.provider.domain.UmsAdminLoginLog;

/**
 * <p>
 * 后台用户登录日志表 服务类
 * </p>
 *
 * @author kimi
 * @since 2021-03-10
 */
public interface UmsAdminLoginLogService{

    /**
     * 新增日志
     * @author 郭富城
     * @date 2021/3/10 16:09
     * @param [umsAdminLoginLog] 日志参数
     * @return int
     */
    int insert(UmsAdminLoginLog umsAdminLoginLog);
}
