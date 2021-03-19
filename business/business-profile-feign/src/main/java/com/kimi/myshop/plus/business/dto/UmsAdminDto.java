package com.kimi.myshop.plus.business.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 郭富城
 */
@Data
public class UmsAdminDto implements Serializable {

    private static final long serialVersionUID = -7332244008502359272L;
    private Long id;
    private String username;
    private String icon;
    private String email;
    private String nickName;
    private String note;
    private Date createTime;
    private Date loginTime;
    private Integer status;
}
