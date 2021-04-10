package com.kimi.myshop.plus.business.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.kimi.myshop.plus.business.annotation.Log;
import com.kimi.myshop.plus.business.dto.excel.RoleExcel;
import com.kimi.myshop.plus.business.dto.excel.UserExcel;
import com.kimi.myshop.plus.commons.dto.ResponseResult;
import com.kimi.myshop.plus.provider.api.UmsRoleService;
import com.kimi.myshop.plus.provider.domain.Role;
import com.kimi.myshop.plus.provider.dto.RoleDto;
import com.kimi.myshop.plus.provider.dto.RoleQueryCriteria;
import com.kimi.myshop.plus.provider.dto.UserDto;
import com.kimi.myshop.plus.provider.dto.UserQueryCriteria;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Log("导出用户数据")
    @GetMapping("/download")
    public ResponseResult<Object> download(HttpServletResponse response, RoleQueryCriteria criteria) throws IOException {
        List<RoleDto> queryAll = umsRoleService.selectAll(criteria);
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("easyExcel", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), UserExcel.class).autoCloseStream(Boolean.FALSE).sheet("用户数据")
                    .doWrite(data(queryAll));
        } catch (Exception e) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
        return new ResponseResult<>(ResponseResult.CodeStatus.OK);

    }

    private List<RoleExcel> data(List<RoleDto> toExcel) {
        List<RoleExcel> result = new ArrayList<>();

        return result;
    }

}
