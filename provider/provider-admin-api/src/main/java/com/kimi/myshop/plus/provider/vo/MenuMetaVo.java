package com.kimi.myshop.plus.provider.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 路由Meta数据
 * @author 郭富城
 */
@Data
@AllArgsConstructor
public class MenuMetaVo implements Serializable {
    private String title;

    private String icon;
}
