package com.kimi.myshop.plus.provider;

import com.kimi.myshop.plus.config.SentinelAspectConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {ProviderAdminLoginLogApplication.class, SentinelAspectConfiguration.class})
@MapperScan(basePackages = "com.kimi.myshop.plus.provider.mapper")
public class ProviderAdminLoginLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderAdminLoginLogApplication.class,args);
    }
}
