package com.micro.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.disk.bean.FileBean;
import com.micro.disk.service.FileService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.service.UserService;
import com.micro.mvc.UserInfoUtils;
import com.micro.pojo.EditBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/disk/fileedit")
public class FileEditController {

    @Reference(check = false)
    private FileService fileService;
    @Reference(check =false)
    private UserService userService;
    @PostMapping("/addFile")
    public Result addFile(EditBean editBean, HttpServletRequest request){
        SessionUserBean sessionUserBean = UserInfoUtils.getBean(request);
        FileBean fileBean = null;
        try {
            fileBean = fileService.addFile(editBean.getPid(),editBean.getFilename(),editBean.getContent().getBytes(),sessionUserBean.getNickname(),sessionUserBean.getId());
        }catch (Exception e){
            return ResultUtils.error(e.getMessage());
        }
        return ResultUtils.success("添加文件成功",fileBean);
    }
}
