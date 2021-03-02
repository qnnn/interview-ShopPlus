package com.kimi.myshop.plus.provider.api;

import com.kimi.myshop.plus.provider.domain.UmsAdmin;

/**
 *
 *
 * @author chenmc
 * @date 2020/11/21 1:35
 * @param
 * @return
 */
public interface UmsAdminService {

    /**
     *  新增用户
     *
     * @author chenmc
     * @date 2020/11/21 12:29
     * @param umsAdmin {@link UmsAdmin}
     * @return int 大于0表示新增成功
     */
    int insert(UmsAdmin umsAdmin);

    /**
     *  获取用户
     *
     * @author chenmc
     * @date 2020/11/21 14:06
     * @param username {@link UmsAdmin}
     * @return com.kimi.myshop.plus.provider.domain.UmsAdmin
     */
    UmsAdmin get(String username);

    /**
     *  获取用户
     *
     * @author chenmc
     * @date 2020/11/21 14:06
     * @param umsAdmin {@link UmsAdmin}
     * @return com.kimi.myshop.plus.provider.domain.UmsAdmin
     */
    UmsAdmin get(UmsAdmin umsAdmin);

    /**
     * 更新个人信息
     *
     * @author chenmc
     * @date 2021/1/27 17:17
     * @param [umsAdmin]
     * @return int
     */
    int update(UmsAdmin umsAdmin);

    /**
     * 刷新登录时间
     * @param username
     * @return
     */
    void updateLoginTime(String username);

    /**
     * 修改头像
     * @param username 用户名
     * @param path 头像路径
     * @return
     */
    int modifyIcon(String username,String path);

    /**
     * 修改密码
     * @param username 用户名
     * @param password 密码
     * @return
     */
    int modifyPassword(String username,String password);
}
