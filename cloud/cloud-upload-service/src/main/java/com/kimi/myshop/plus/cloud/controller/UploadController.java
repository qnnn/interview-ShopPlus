package com.kimi.myshop.plus.cloud.controller;

import com.kimi.myshop.plus.cloud.dto.FileInfo;
import com.kimi.myshop.plus.commons.dto.ResponseResult;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping(value = "upload")
public class UploadController {

    /**
     * 1 初始化用户身份信息(secretId, secretKey)
     */
//    COSCredentials cred = new BasicCOSCredentials("100017587557", "Hst123456.");
    COSCredentials cred = new BasicCOSCredentials("AKIDLZGhutNVQ5UgcHDspWx9D0f4JL7klUdX", "rSlqgFwMVO2f21ljRu62vb091wYweU5V");
    /**
     * 2 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
     */
    ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
    /**
     * 3 生成cos客户端
     */
    COSClient cosclient = new COSClient(cred, clientConfig);
    /**
     * bucket名需包含appid
     */
    String bucketName = "data-repository-1304879813";

    /**
     * 基础路径
     */
    String basePath = "https://data-repository-1304879813.cos.ap-guangzhou.myqcloud.com/";


    @PostMapping(value = "")
    public ResponseResult<FileInfo> upload(MultipartFile multipartFile){
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        assert fileName != null;
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String newName = UUID.randomUUID() + "." + suffix;


        try {

            InputStream input = new ByteArrayInputStream(multipartFile.getBytes());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 从输入流上传必须制定content length, 否则http客户端可能会缓存所有数据，存在内存OOM的情况
            objectMetadata.setContentLength(multipartFile.getSize());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, newName,input,objectMetadata);

            // 限流单位bit/s, 此处测试1MB/s的上传宽带限制
//            putObjectRequest.setTrafficLimit(8*1024*1024);

            // 设置存储类型，默认标准是（Standard），低频存储只有30天（standard_ia）
            putObjectRequest.setStorageClass(StorageClass.Standard_IA);

            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);

            return new ResponseResult<FileInfo>(ResponseResult.CodeStatus.OK, "文件上传成功", new FileInfo(basePath + newName));

        } catch (IOException e) {
            return new ResponseResult<FileInfo>(ResponseResult.CodeStatus.FAIL, "文件上传失败，请重试");
        }
//        finally {
////            cosclient.shutdown();
//        }
    }
}
