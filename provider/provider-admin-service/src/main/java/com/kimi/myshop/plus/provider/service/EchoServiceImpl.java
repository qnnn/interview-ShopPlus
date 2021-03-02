package com.kimi.myshop.plus.provider.service;

import com.kimi.myshop.plus.provider.api.EchoService;
import org.apache.dubbo.config.annotation.Service;

/**
 * 注册服务实现.
 * @author 郭富城
 */
@Service(version = "1.0.0")
public class EchoServiceImpl implements EchoService {
    @Override
    public String echo(String string) {
        return "hello Dubbo"+string;
    }
}
