package com.kimi.myshop.plus.business.controller;

import com.kimi.myshop.plus.commons.dto.ResponseResult;
import com.kimi.myshop.plus.provider.api.UmsRoleService;
import com.kimi.myshop.plus.provider.dto.RoleQueryCriteria;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色管理
 *
 * @author 郭富城
 */
@RestController
@RequestMapping(value = "system/role")
public class RoleController {

    @Reference(version = "1.0.0",timeout = 2000)
    private UmsRoleService umsRoleService;

    @GetMapping
    public ResponseResult<Object> query(RoleQueryCriteria criteria, Pageable pageable){
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,umsRoleService.selectAll(criteria, pageable));
    }
}
