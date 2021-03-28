package com.kimi.myshop.plus.business.controller;


import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.JSON;
import com.kimi.myshop.plus.business.annotation.Log;
import com.kimi.myshop.plus.business.dto.excel.UserExcel;
import com.kimi.myshop.plus.commons.dto.ResponseResult;
import com.kimi.myshop.plus.commons.utils.EasyExcelHelper;
import com.kimi.myshop.plus.commons.utils.FileUtil;
import com.kimi.myshop.plus.provider.api.UmsAdminService;
import com.kimi.myshop.plus.provider.api.UmsUserService;
import com.kimi.myshop.plus.provider.domain.UmsAdmin;
import com.kimi.myshop.plus.provider.domain.User;
import com.kimi.myshop.plus.provider.dto.AbridgedRoleDto;
import com.kimi.myshop.plus.provider.dto.UserDto;
import com.kimi.myshop.plus.provider.dto.UserQueryCriteria;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户管理
 *
 * @author 郭富城
 */
@RestController
@RequestMapping(value = "system/user")
public class UserController {

    @Reference(version = "1.0.0", timeout = 2000)
    private UmsUserService umsUserService;

    @Reference(version = "1.0.0", timeout = 2000)
    private UmsAdminService umsAdminService;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    private static final String DEFAULT_ICON = "https://data-repository-1304879813.cos.ap-guangzhou.myqcloud.com/19cebb68-0ed4-4fc5-ad14-d46ffdd96c21.png";

    @GetMapping
    public ResponseResult<Object> query(UserQueryCriteria criteria, Pageable pageable) {
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, umsUserService.selectAll(criteria, pageable));
    }

    @Log("新增用户")
    @PostMapping("/add")
    public ResponseResult<Object> create(@RequestBody User user) {
        if (umsAdminService.get(user.getUsername()) != null) {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "用户名已存在！");
        }
        if (umsAdminService.getByEmail(user.getEmail()) != null) {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "邮箱已存在！");
        }
        user.setPassword(passwordEncoder.encode("123456"));
        user.setCreateTime(new Date());
        user.setLoginTime(new Date());
        user.setIcon(DEFAULT_ICON);
        umsUserService.save(user);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "新增用户成功");
    }

    @Log("修改用户")
    @PostMapping("/edit")
    public ResponseResult<Object> update(@RequestBody User user) {
        UmsAdmin comparator = umsAdminService.getByEmail(user.getEmail());
        // 存在相同
        if (comparator != null && !comparator.getId().equals(user.getId())) {
            return new ResponseResult<>(ResponseResult.CodeStatus.FAIL, "邮箱已存在！");
        }
        umsUserService.save(user);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "修改用户成功");
    }

    @Log("导出用户数据")
    @GetMapping("/download")
    public ResponseResult<Object> download(HttpServletResponse response, UserQueryCriteria criteria) throws IOException {
        List<UserDto> queryAll = umsUserService.selectAll(criteria);
        /**
         * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
         *
         * @since 2.1.1
         */
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), UserExcel.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(data(queryAll));
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
        return null;

    }

    @Log("删除用户")
    @PostMapping("/delete")
    public ResponseResult<Object> delete(@RequestBody Set<Long> ids) {
        umsUserService.deleteMulti(ids);
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
