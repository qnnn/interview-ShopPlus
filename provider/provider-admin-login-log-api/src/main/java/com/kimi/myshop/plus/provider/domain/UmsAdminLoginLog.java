package com.kimi.myshop.plus.provider.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 后台用户登录日志表
 * </p>
 *
 * @author kimi
 * @since 2021-03-10
 */
@Data
//@Table(name = "ums_admin_login_log")
@EqualsAndHashCode(callSuper = false)
public class UmsAdminLoginLog implements Serializable {


    private static final long serialVersionUID = 7431724466765975705L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long adminId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    private String ip;

    private String address;

    /**
     * 浏览器登录类型
     */
    private String userAgent;


}
