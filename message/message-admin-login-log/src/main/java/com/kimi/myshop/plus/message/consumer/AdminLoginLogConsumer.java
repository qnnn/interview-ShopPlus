package com.kimi.myshop.plus.message.consumer;

import com.kimi.myshop.plus.commons.utils.MapperUtils;
import com.kimi.myshop.plus.provider.api.UmsAdminLoginLogService;
import com.kimi.myshop.plus.provider.domain.UmsAdminLoginLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminLoginLogConsumer {

    @Reference(version = "1.0.0")
    private UmsAdminLoginLogService umsAdminLoginLogService;

    @StreamListener(Sink.INPUT)
    public void receiveLoginLog(String adminLoginLog) throws Exception {
        UmsAdminLoginLog umsAdminLoginLog = MapperUtils.json2pojo(adminLoginLog,UmsAdminLoginLog.class);
        umsAdminLoginLogService.insert(umsAdminLoginLog);
        log.info("成功接收消息！");
    }
}
