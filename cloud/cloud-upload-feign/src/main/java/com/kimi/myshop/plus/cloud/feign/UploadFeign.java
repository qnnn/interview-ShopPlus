package com.kimi.myshop.plus.cloud.feign;

import com.kimi.myshop.plus.cloud.feign.fallback.UploadFallBack;
import com.kimi.myshop.plus.config.FeignRequestConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "cloud-upload",path = "upload",configuration = FeignRequestConfig.class,fallback = UploadFallBack.class)
public interface UploadFeign {

    /**
     * 文件上传
     * @param multipartFile
     * @return
     */
    @PostMapping(value = "")
    String upload(@RequestPart(value = "multipartFile") MultipartFile multipartFile);
}
