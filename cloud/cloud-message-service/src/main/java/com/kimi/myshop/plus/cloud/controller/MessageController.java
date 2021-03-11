package com.kimi.myshop.plus.cloud.controller;

import com.kimi.myshop.plus.cloud.dto.AdminLoginLogDTO;
import com.kimi.myshop.plus.cloud.api.MessageProducer;
import com.kimi.myshop.plus.commons.dto.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "message")
public class MessageController {

    @Resource
    private MessageProducer producer;

//    @Resource
//    private MessageProducerImpl producer;

    @PostMapping(value = "admin/login/log")
    public ResponseResult<Void> sendAdminLoginLog(@RequestBody AdminLoginLogDTO dto){
        boolean result = producer.sendAdminLoginLog(dto);
        // 发送成功
        if (result){
            return new ResponseResult<Void>(ResponseResult.CodeStatus.OK, "消息发送成功");
        }

        // 发送失败
        else {
            return new ResponseResult<Void>(ResponseResult.CodeStatus.FAIL, "消息发送失败");
        }
    }
}
