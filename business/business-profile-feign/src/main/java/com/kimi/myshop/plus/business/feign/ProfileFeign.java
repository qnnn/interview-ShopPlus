package com.kimi.myshop.plus.business.feign;

import com.kimi.myshop.plus.business.dto.params.IconParam;
import com.kimi.myshop.plus.business.dto.params.ProfileParam;
import com.kimi.myshop.plus.business.feign.fallback.ProfileFeignFallback;
import com.kimi.myshop.plus.config.FeignRequestConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *  在注册中心调用business-profile服务，定义当前feignClient的统一前缀为profile
 *
 * @author 郭富城
 * @date 2021/1/24 20:18
 * @param
 * @return
 */

@FeignClient(value = "business-profile",path = "profile",configuration = FeignRequestConfig.class,fallback = ProfileFeignFallback.class)
public interface ProfileFeign {

    /**
     * 查看个人信息
     * @param username 用户名
     * @return 用户名
     */
    @GetMapping(value = "info/{username}")
    String info(@PathVariable String username);

    /**
     * 更新个人信息
     * @param profileParam 个人信息DTO
     * @return
     */
    @PostMapping(value = "update")
    String update(@RequestBody ProfileParam profileParam);

    /**
     * 修改头像
     * @param iconParam
     * @return
     */
    @PostMapping(value = "modify/icon")
    String modifyIcon(@RequestBody IconParam iconParam);
}
