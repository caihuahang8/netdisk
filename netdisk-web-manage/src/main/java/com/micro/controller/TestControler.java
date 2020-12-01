package com.micro.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestControler {


    @RequestMapping("/say")
    public String say() {
        String ni = new String("nimasile");
        return ni;
    }
        @RequestMapping("/sayHi")
        public String sayHi() {
            return "test";
        }
}
