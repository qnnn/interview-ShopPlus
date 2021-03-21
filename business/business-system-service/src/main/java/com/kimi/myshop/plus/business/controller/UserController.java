package com.kimi.myshop.plus.business.controller;


import com.kimi.myshop.plus.commons.dto.ResponseResult;
import com.kimi.myshop.plus.provider.api.UmsUserService;
import com.kimi.myshop.plus.provider.dto.UserQueryCriteria;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户管理
 *
 * @author 郭富城
 */
@RestController
@RequestMapping(value = "system/user")
public class UserController {

    @Reference(version = "1.0.0",timeout = 2000)
    private UmsUserService umsUserService;


    @GetMapping
    public ResponseResult<Object> query(UserQueryCriteria criteria, Pageable pageable){


        return new ResponseResult<>(ResponseResult.CodeStatus.OK,umsUserService.selectAll(criteria,pageable));
    }











}
