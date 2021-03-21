package com.kimi.myshop.plus.business.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.kimi.myshop.plus.business.controller.fallback.ProfileControllerFallback;
import com.kimi.myshop.plus.business.dto.UmsAdminDto;
import com.kimi.myshop.plus.business.dto.params.IconParam;
import com.kimi.myshop.plus.business.dto.params.PasswordParam;
import com.kimi.myshop.plus.business.dto.params.ProfileParam;
import com.kimi.myshop.plus.commons.dto.ResponseResult;
import com.kimi.myshop.plus.business.annotation.Log;
import com.kimi.myshop.plus.provider.api.UmsAdminService;
import com.kimi.myshop.plus.provider.domain.UmsAdmin;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 个人信息管理
 *
 * @author 郭富城
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "profile")
public class ProfileController {

    @Reference(version = "1.0.0",timeout = 5000)
    private UmsAdminService umsAdminService;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 获取个人信息
     * @param username 用户名
     * @return 返回结果
     */
    @GetMapping(value = "info/{username}")
    @SentinelResource(value = "getInfo", fallback = "getInfoFallback", fallbackClass = ProfileControllerFallback.class)
    public ResponseResult<UmsAdminDto> info(@PathVariable("username") String username){
        UmsAdmin umsAdmin = umsAdminService.get(username);
        UmsAdminDto umsAdminDto = new UmsAdminDto();
        BeanUtils.copyProperties(umsAdmin,umsAdminDto);
        return new ResponseResult<UmsAdminDto>(ResponseResult.CodeStatus.OK,"查询用户信息",umsAdminDto);
    }

    /**
     * 更新头像
     * @param iconParam 头像信息
     * @return 返回成功与否
     */
    @PostMapping(value = "modify/icon")
    @Log("修改头像")
    public ResponseResult<Void> modifyIcon(@RequestBody IconParam iconParam){

        int result = umsAdminService.modifyIcon(iconParam.getUsername(),iconParam.getPath());
        // 成功
        if (result>0){
            return new ResponseResult<Void>(ResponseResult.CodeStatus.OK,"更新头像成功");
        }
        // 失败
        else {
            return new ResponseResult<Void>(ResponseResult.CodeStatus.FAIL,"更新头像失败");
        }
    }



    @PostMapping(value = "modify/password")
    @Log("修改密码")
    public ResponseResult<Void> modifyPassword(@RequestBody PasswordParam passwordParam) {
        UmsAdmin umsAdmin = umsAdminService.get(passwordParam.getUsername());

        // 密码正确
        if (passwordEncoder.matches(passwordParam.getOldPassword(), umsAdmin.getPassword())) {
            int result = umsAdminService.modifyPassword(umsAdmin.getUsername(), passwordParam.getNewPassword());
            if (result > 0) {
                return new ResponseResult<>(ResponseResult.CodeStatus.OK, "修改密码成功");
            }
        }

        // 密码错误
        else {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "密码错误！");
        }

        return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "修改密码失败");
    }

    /**
     *  更新个人信息
     *
     * @author 郭富城
     * @date 2021/1/27 16:10
     * @param [profileParam] 个人信息参数
     * @return com.kimi.myshop.plus.commons.dto.ResponseResult<java.lang.Void>
     */
    @PostMapping(value = "update")
    @Log("更改个人信息")
    public ResponseResult<Void> update(@RequestBody ProfileParam profileParam){

        UmsAdmin umsAdmin=new UmsAdmin();
        BeanUtils.copyProperties(profileParam,umsAdmin);
        int result = umsAdminService.update(umsAdmin);
        // 成功
        if (result>0){
            return new ResponseResult<Void>(ResponseResult.CodeStatus.OK,"更新个人信息成功");
        }
        // 失败
        else {
            return new ResponseResult<Void>(ResponseResult.CodeStatus.FAIL,"更新个人信息失败");
        }
    }
}
