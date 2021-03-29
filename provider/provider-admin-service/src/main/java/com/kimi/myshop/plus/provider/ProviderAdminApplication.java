package com.kimi.myshop.plus.provider;

import com.kimi.myshop.plus.config.SentinelAspectConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 提供数据库服务.
 * @author 郭富城
 */
@SpringBootApplication(scanBasePackageClasses = {ProviderAdminApplication.class, SentinelAspectConfiguration.class})
@MapperScan(basePackages = "com.kimi.myshop.plus.provider.mapper")
@EntityScan(basePackages = "com.kimi.myshop.plus.provider.domain")
public class ProviderAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderAdminApplication.class,args);
    }
}
