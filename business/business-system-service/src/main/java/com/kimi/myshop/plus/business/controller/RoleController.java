package com.kimi.myshop.plus.business.controller;

import com.kimi.myshop.plus.business.annotation.Log;
import com.kimi.myshop.plus.commons.dto.ResponseResult;
import com.kimi.myshop.plus.provider.api.UmsRoleService;
import com.kimi.myshop.plus.provider.domain.Role;
import com.kimi.myshop.plus.provider.dto.RoleQueryCriteria;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * 角色管理
 *
 * @author 郭富城
 */
@RestController
@RequestMapping(value = "system/role")
public class RoleController {

    @Reference(version = "1.0.0",timeout = 2000)
    private UmsRoleService umsRoleService;

    @GetMapping
    public ResponseResult<Object> query(RoleQueryCriteria criteria, Pageable pageable){
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,umsRoleService.selectAll(criteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseResult<Object> query(@PathVariable Long id){
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,umsRoleService.findById(id));
    }

    @Log("新增角色")
    @PostMapping("/add")
    public ResponseResult<Object> create(@RequestBody Role role) {
        umsRoleService.create(role);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"创建角色成功！");
    }

    @Log("修改角色")
    @PostMapping("/edit")
    public ResponseResult<Object> update(@RequestBody Role role){
        umsRoleService.update(role);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"修改角色成功！");
    }


    @Log("修改角色菜单")
    @PostMapping("/menu")
    public ResponseResult<Object> updateMenu(@RequestBody Role role){
        umsRoleService.updateMenu(role);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"修改角色对应菜单成功！");
    }

    @Log("删除角色")
    @PostMapping("/delete")
    public ResponseResult<Object> delete(@RequestBody Set<Long> ids){
        for (Long id : ids) {
            if (id == 5 ){
                return new ResponseResult<>(ResponseResult.CodeStatus.FAIL,"禁止删除管理员！");
            }
        }
        umsRoleService.delete(ids);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"删除角色成功！");
    }

}
