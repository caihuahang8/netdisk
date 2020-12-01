package com.micro.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.micro.disk.user.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/test")
public class TestControler {

        @Reference(check = false)
        private TestService testService;
        @RequestMapping("/sayHi")
        @ResponseBody
        public String sayHi(String name) {
            return testService.sayHi(name);
        }
}
