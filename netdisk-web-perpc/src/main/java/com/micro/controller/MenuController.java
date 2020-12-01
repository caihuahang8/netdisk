package com.micro.controller;

import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.xml.MenuBean;
import com.micro.xml.XmlUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @PostMapping("/findList")
    public Result findMenu(String roleid){
        try{
            List<MenuBean>  menuBeans = XmlUtils.parseMenusxml();
            return ResultUtils.success("查找菜单",menuBeans);
        }catch (Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }
}
