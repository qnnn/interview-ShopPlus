package com.kimi.myshop.plus.business.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *  登录参数.
 *
 * @author 郭富城
 * @date 2020/11/22 1:06
 * @param
 * @return
 */
@Data
public class LoginParam implements Serializable {

    private String username;
    private String password;
}
