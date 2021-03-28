package com.kimi.myshop.plus.business.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.kimi.myshop.plus.business.annotation.Log;
import com.kimi.myshop.plus.business.dto.excel.UserExcel;
import com.kimi.myshop.plus.commons.dto.ResponseResult;
import com.kimi.myshop.plus.provider.api.UmsMenuService;
import com.kimi.myshop.plus.provider.domain.Menu;
import com.kimi.myshop.plus.provider.domain.UmsAdmin;
import com.kimi.myshop.plus.provider.domain.User;
import com.kimi.myshop.plus.provider.dto.MenuDto;
import com.kimi.myshop.plus.provider.dto.MenuQueryCriteria;
import com.kimi.myshop.plus.provider.dto.UserDto;
import com.kimi.myshop.plus.provider.dto.UserQueryCriteria;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 菜单管理
 *
 * @author 郭富城
 */
@RestController
@RequestMapping(value = "system/menu")
public class MenuController {

    @Reference(version = "1.0.0")
    private UmsMenuService umsMenuService;

    @GetMapping
    public ResponseResult<Object> query(MenuQueryCriteria criteria) throws IllegalAccessException {
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, umsMenuService.selectAll(criteria));
    }


    @PostMapping("/parent")
    public ResponseResult<Object> getSuperior(@RequestBody List<Long> ids) {
        Set<MenuDto> menuDtos = new LinkedHashSet<>();
        if (CollectionUtil.isNotEmpty(ids)) {
            for (Long id : ids) {
                MenuDto menuDto = umsMenuService.findById(id);
                menuDtos.addAll(umsMenuService.getParent(menuDto, new ArrayList<>()));
            }
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, umsMenuService.getTree(new ArrayList<>(menuDtos)));
        }
        // 若为查找顶级结点，且无孩子结点
        return new ResponseResult<>(ResponseResult.CodeStatus.OK , umsMenuService.getMenus(null));
    }

    /**
     *  懒加载子类
     * @param pid
     * @return
     */
    @GetMapping("/lazy")
    public ResponseResult<Object> lazyGet(@RequestParam Long pid){
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,umsMenuService.getMenus(pid));
    }

    @Log("新增菜单")
    @PostMapping("/add")
    public ResponseResult<Object> create(@RequestBody Menu menu) {

        return null;
    }

    @Log("修改菜单")
    @PostMapping("/edit")
    public ResponseResult<Object> update(@RequestBody Menu menu) {

        return null;
    }

    @Log("删除用户")
    @PostMapping("/delete")
    public ResponseResult<Object> delete(@RequestBody Set<Long> ids) {
        umsMenuService.deleteMulti(ids);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "删除用户成功");
    }


    private List<UserExcel> data(List<UserDto> toExcel) {
        List<UserExcel> result = new ArrayList<>();
        for (UserDto userDto : toExcel) {
            UserExcel temp = new UserExcel();
            temp.setUsername(userDto.getUsername());
            temp.setEmail(userDto.getEmail());
            temp.setIcon(userDto.getIcon());
            temp.setCreateTime(userDto.getCreateTime());
            temp.setLoginTime(userDto.getLoginTime());
            temp.setNickName(userDto.getNickName());
            temp.setNote(userDto.getNote());
            temp.setRoles(userDto.getRoles().toString());
            temp.setStatus(userDto.getStatus() == 1 ? "激活" : "冻结");
            result.add(temp);
        }
        return result;
    }
}
