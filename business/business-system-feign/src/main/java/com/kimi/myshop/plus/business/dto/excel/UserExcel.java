package com.kimi.myshop.plus.business.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimi.myshop.plus.provider.domain.Role;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 郭富城
 */
@Data
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(30)
public class UserExcel {

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("头像")
    private String icon;

    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("昵称")
    private String nickName;

    @ExcelProperty("标注")
    private String note;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("最后登录时间")
    private Date loginTime;

    @ExcelProperty("状态")
    private String status;

    @ExcelProperty("角色")
    @ColumnWidth(50)
    private String roles;
}
