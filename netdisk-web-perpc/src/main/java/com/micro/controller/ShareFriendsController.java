package com.micro.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.ShareBean;
import com.micro.disk.service.FileService;
import com.micro.disk.service.ShareService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.mvc.UserInfoUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/disk/sharebyfriends")
public class ShareFriendsController {

    @Reference(check=false)
    private ShareService shareService;
    @Reference(check=false)
    private FileService fileService;

    /**
     * 查看我的好友分享
     * @param pi
     * @param status
     * @param request
     * @return
     */
    @PostMapping("/findList")
    public Result findList(PageInfo<ShareBean> pi, Integer status, HttpServletRequest request){
        try {
            SessionUserBean user = UserInfoUtils.getBean(request);
            return ResultUtils.success("查找成功",shareService.findFriendsShare(pi.getPage(), pi.getLimit(), user.getId(), status));
        }catch (Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }

    /**
     * 查看好友分享的文件的内容
     * @param id 分享id
     * @param pid 文件夹id
     * @return
     */
    @PostMapping("/findShareList")
    public Result findShareList(String id,String pid){
        try{
            return ResultUtils.success("查询成功",shareService.findShareFileListFromFriends(id, pid));
        }catch(Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }

}
