package com.kimi.myshop.plus.business;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * OAuth2服务.
 * @author 郭富城
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class BusinessOauth2Application {
    public static void main(String[] args) {
        SpringApplication.run(BusinessOauth2Application.class,args);
    }
}
