package com.kimi.myshop.plus.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackageClasses = {CloudUploadApplication.class},scanBasePackages = "com.kimi.myshop.plus.business")
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = "com.kimi.myshop.plus.business")
@EntityScan(basePackages = "com.kimi.myshop.plus.business")
public class CloudUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudUploadApplication.class,args);
    }
}
