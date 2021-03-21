package com.kimi.myshop.plus.business.dto.params;

import lombok.Data;

import java.io.Serializable;


/**
 * @author 郭富城
 */
@Data
public class PasswordParam implements Serializable {


    private static final long serialVersionUID = -3650327863918379543L;

    private String username;
    private String oldPassword;
    private String newPassword;

}
