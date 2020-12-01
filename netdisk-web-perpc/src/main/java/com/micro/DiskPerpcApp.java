package com.micro;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@NacosPropertySource(dataId = "disk-web-perpc",groupId="disk",autoRefreshed=true)
@EnableDubboConfiguration
@SpringBootApplication
public class DiskPerpcApp {
    public static void main(String[] args) {
        SpringApplication.run(DiskPerpcApp.class, args);
    }
}
