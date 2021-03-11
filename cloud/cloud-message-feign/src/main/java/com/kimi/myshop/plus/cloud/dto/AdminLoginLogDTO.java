package com.kimi.myshop.plus.cloud.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * 日志数据传输对象
 * @author 郭富城
 */
@Data
public class AdminLoginLogDTO implements Serializable {


    private static final long serialVersionUID = 2683229815888373671L;
    private Long id;

    private Long adminId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    private String ip;

    private String address;

    private String userAgent;
}
