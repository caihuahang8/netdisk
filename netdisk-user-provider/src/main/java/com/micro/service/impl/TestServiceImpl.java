package com.micro.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.registry.zookeeper.ZookeeperRegistry;
import com.micro.disk.user.service.TestService;
import org.springframework.stereotype.Component;

@Service(interfaceClass= TestService.class)
@Component
public class TestServiceImpl implements TestService {
    @Override
    public String sayHi(String name) {
        return "chenggong";
    }
}
