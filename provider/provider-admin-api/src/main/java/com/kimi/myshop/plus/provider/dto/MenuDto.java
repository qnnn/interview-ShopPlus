/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.kimi.myshop.plus.provider.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kimi.myshop.plus.provider.domain.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author 郭富城
 */
@Getter
@Setter
public class MenuDto implements Serializable {

    private Long id;

    private Long pid;

    private String name;

    private String value;

    private String routerName;

    private Boolean externalLink;

    private String icon;

    private Integer type;

    private String uri;

    private Boolean isParent;

    private List<MenuDto> children;

    private Integer status;

    private Date createTime;

    private Integer sort;

    /**
     * 返回前端，树形菜单TreeSelect得到子标签
     */
    public String getLabel() {
        return name;
    }

    /**
     * 树形菜单所需
     */
    public Boolean getHasChildren() {
        return isParent;
    }

    /**
     *  树形菜单所需
     */
    public Boolean getLeaf(){
        return !isParent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuDto menuDto = (MenuDto) o;
        return Objects.equals(id, menuDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
