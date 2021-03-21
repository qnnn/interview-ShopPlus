package com.kimi.myshop.plus.provider.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author 郭富城
 */
@Getter
@Setter
public class UserDto implements Serializable {

    private Long id;

    private Set<AbridgedRoleDto> roles;

    private String username;

    private String icon;

    private String email;

    private String nickName;

    private String note;

    private Date createTime;

    private Date loginTime;

    private Integer status;

}
