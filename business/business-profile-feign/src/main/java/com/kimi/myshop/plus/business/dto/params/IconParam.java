package com.kimi.myshop.plus.business.dto.params;

import lombok.Data;

import java.io.Serializable;

/**
 *  Icon DTO
 *
 * @author 郭富城
 * @date 2021/1/29 14:04
 * @param
 * @return
 */
@Data
public class IconParam implements Serializable {

    private String username;
    private String path;
}
