package com.micro;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.micro.config.StartupRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
@NacosPropertySource(dataId = "disk-service-provider",groupId="disk",autoRefreshed=true)
@EnableDubboConfiguration
@SpringBootApplication
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
    }

    @Bean
    public StartupRunner startupRunner() {
        return new StartupRunner();
    }
}
