package com.kimi.myshop.plus.provider.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * @author 郭富城
 */
@Getter
@Setter
@Entity
@Table(name = "ums_permission")
public class Menu implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 父级菜单
     */
    @Column(name = "pid")
    private Long pid;

    /**
     * 菜单名称
     */
    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @Column(name = "router_name")
    private String routerName;

    @Column(columnDefinition = "bit(1) default 0")
    private Boolean externalLink;

    /**
     * 菜单图标
     */
    @Column(name = "icon")
    private String icon;

    @Column(name = "type")
    private Integer type;

    @Column(name = "uri")
    private String uri;

    @Column(columnDefinition = "bit(1) default 0")
    private Boolean isParent;

    /**
     * 连接 role 表
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "menus")
    private Set<Role> roles;

    /**
     * 菜单状态
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "sort")
    private Integer sort;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
