package com.kimi.myshop.plus.business.dto.params;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class ProfileParam implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String icon;
    private String email;
    private String nickName;
    private String note;
    private Date createTime;
    private Date loginTime;
    private Integer status;
}
