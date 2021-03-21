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
public class RoleDto implements Serializable {
    private Long id;

    private String name;

    private String description;

    private String adminCount;

    private Set<MenuDto> menus;

    private Date createTime;

    private Integer status;

    private Integer sort;
}
