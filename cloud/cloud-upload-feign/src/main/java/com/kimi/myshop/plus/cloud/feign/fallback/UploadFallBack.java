package com.kimi.myshop.plus.cloud.feign.fallback;

import com.kimi.myshop.plus.cloud.feign.UploadFeign;
import com.kimi.myshop.plus.commons.dto.ResponseResult;
import com.kimi.myshop.plus.commons.utils.MapperUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
/**
 * 上传失败回调.
 *
 * @author 郭富城
 * @date 2021/1/28 20:29
 * @param
 * @return
 */
@Component
public class UploadFallBack implements UploadFeign {
    public static final String FAIL_MESSAGE="请求失败，请检查网络";

    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            return MapperUtils.obj2json(new ResponseResult<Void>(ResponseResult.CodeStatus.BREAKING,FAIL_MESSAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
