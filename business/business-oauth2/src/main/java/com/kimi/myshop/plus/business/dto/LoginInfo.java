package com.kimi.myshop.plus.business.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *  登录信息.
 *
 * @author chenmc
 * @date 2020/11/22 16:23
 * @param
 * @return
 */
@Data
public class LoginInfo implements Serializable {

    private String name;
    private String avatar;
}
