package com.kimi.myshop.plus.cloud.api;

import com.kimi.myshop.plus.cloud.dto.AdminLoginLogDTO;

/**
 * @author 郭富城
 */
public interface MessageProducer {

    boolean sendAdminLoginLog(AdminLoginLogDTO adminLoginLogDTO);
}
