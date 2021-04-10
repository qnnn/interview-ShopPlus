package com.kimi.myshop.plus.business.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.kimi.myshop.plus.business.annotation.Log;
import com.kimi.myshop.plus.commons.dto.ResponseResult;
import com.kimi.myshop.plus.commons.utils.SecurityUtils;
import com.kimi.myshop.plus.provider.api.UmsMenuService;
import com.kimi.myshop.plus.provider.domain.Menu;
import com.kimi.myshop.plus.provider.dto.MenuDto;
import com.kimi.myshop.plus.provider.dto.MenuQueryCriteria;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
     * @param pid 父级id
     * @return
     */
    @GetMapping("/lazy")
    public ResponseResult<Object> lazyGet(@RequestParam Long pid){
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,umsMenuService.getMenus(pid));
    }

    @Log("新增菜单")
    @PostMapping("/add")
    public ResponseResult<Object> create(@RequestBody Menu menu) {
        umsMenuService.create(menu);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"新增菜单成功！");
    }

    @Log("修改菜单")
    @PostMapping("/edit")
    public ResponseResult<Object> update(@RequestBody Menu menu) {
        umsMenuService.update(menu);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"修改菜单成功！");
    }

    @Log("删除菜单")
    @PostMapping("/delete")
    public ResponseResult<Object> delete(@RequestBody Set<Long> ids) {
        umsMenuService.deleteMulti(ids);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "删除菜单成功");
    }

    /**
     * 前端获取路由菜单
     * @return 路由菜单
     */
    @GetMapping(value = "/build")
    public ResponseResult<Object> buildMenu(){
        List<MenuDto> menuDtos = umsMenuService.findByUser(SecurityUtils.getCurrentUsername());
        List<MenuDto> tree = umsMenuService.getTree(menuDtos);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,umsMenuService.buildMenus(tree));
    }


}
