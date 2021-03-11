package com.kimi.myshop.plus.cloud.producer;

import com.kimi.myshop.plus.cloud.dto.AdminLoginLogDTO;
import com.kimi.myshop.plus.cloud.api.MessageProducer;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

@Service(version = "1.0.0")
public class MessageProducerImpl implements MessageProducer {


    @Resource
    private Source source;

    @Override
    public boolean sendAdminLoginLog(AdminLoginLogDTO adminLoginLogDTO){
        return this.source.output().send(
                MessageBuilder.withPayload(adminLoginLogDTO).build()
        );
    }

}
