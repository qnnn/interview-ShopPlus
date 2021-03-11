package com.kimi.myshop.plus.cloud.feign;

import com.kimi.myshop.plus.cloud.dto.AdminLoginLogDTO;
import com.kimi.myshop.plus.cloud.feign.fallback.MessageFeignFallback;
import com.kimi.myshop.plus.config.FeignRequestConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 郭富城
 */
@FeignClient(value = "cloud-message",path = "message",configuration = FeignRequestConfig.class,fallback = MessageFeignFallback.class)
public interface MessageFeign {

    @PostMapping(value = "admin/login/log")
    public String sendAdminLoginLog(@RequestBody AdminLoginLogDTO adminLoginLogDTO);
}
