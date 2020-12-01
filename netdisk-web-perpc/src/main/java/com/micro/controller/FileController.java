package com.micro.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.disk.bean.FileListBean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.service.FileService;

import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.service.UserService;
import com.micro.mvc.UserInfoUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/disk/file")
public class FileController {
    @Reference(check = false)
    private FileService fileService;
    @Reference(check=false)
    private UserService userService;
    @RequestMapping("/findList")
    public PageInfo<FileListBean> findList(Integer page, Integer limit, String pid, String orderfield, String ordertype, HttpServletRequest request){
        SessionUserBean sessionUserBean;
        sessionUserBean = UserInfoUtils.getBean(request);
        PageInfo<FileListBean> beanList = fileService.findFileList(page, limit, sessionUserBean.getId(), pid, "all", orderfield, ordertype);
        return beanList;
    }
    @RequestMapping("/findSpecialList")
    public PageInfo<FileListBean> findTypeList(Integer page,Integer limit,String pid,String orderfield,String ordertype,HttpServletRequest request,String typecode){
        SessionUserBean sessionUserBean;
        sessionUserBean = UserInfoUtils.getBean(request);
        PageInfo<FileListBean> beanList = fileService.findFileList(page, limit, sessionUserBean.getId(), pid, typecode, orderfield, ordertype);
        return beanList;
    }
}
