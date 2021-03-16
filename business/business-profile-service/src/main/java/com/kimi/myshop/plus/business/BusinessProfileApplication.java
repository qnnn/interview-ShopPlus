package com.kimi.myshop.plus.business;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableDiscoveryClient
public class BusinessProfileApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusinessProfileApplication.class,args);
    }
}
