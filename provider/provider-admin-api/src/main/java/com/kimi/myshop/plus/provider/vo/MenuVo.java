package com.kimi.myshop.plus.provider.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 展示菜单，前端获取
 * @author 郭富城
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuVo implements Serializable {

    private String name;

    private String path;

    private String redirect;

    private Boolean alwaysShow;

    private Boolean hidden;

    private String component;

    private MenuMetaVo meta;

    private List<MenuVo> children;
}
