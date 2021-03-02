package com.kimi.myshop.plus.provider.service.fallback;

import com.kimi.myshop.plus.provider.domain.UmsAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 用户服务提供者熔断器
 * @author 郭富城
 */
public class UmsAdminServiceFallback {

    private static final Logger logger = LoggerFactory.getLogger(UmsAdminServiceFallback.class);

    /**
     * 此处必须为static
     * 通过用户名得到对象的回调函数
     * @param username
     * @param throwable
     * @return
     */
    public static UmsAdmin getByUsernameFallback(String username,Throwable throwable){

        logger.warn("invoke getByUsernameFallback" + throwable.getClass().getTypeName());
        return null;
    }
}
