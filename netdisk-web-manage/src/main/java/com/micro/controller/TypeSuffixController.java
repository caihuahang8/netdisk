package com.micro.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.disk.service.TypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/type")
public class TypeSuffixController {
    @Reference(check = false)
    private TypeService typeService;

    @RequestMapping("/init")
    public Result  init(){
        try{
            typeService.init();
            return ResultUtils.success("初始化成功",null);
        }catch (Exception e){
            return ResultUtils.error("初始化失败");
        }
    }
}
