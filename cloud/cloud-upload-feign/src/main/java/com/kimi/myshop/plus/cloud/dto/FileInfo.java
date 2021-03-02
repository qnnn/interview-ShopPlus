package com.kimi.myshop.plus.cloud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *  文件信息
 *
 * @author 郭富城
 * @date 2021/1/28 20:06
 * @param
 * @return
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo implements Serializable {
    /**
     *  文件路径
     */
    private String path;
}
