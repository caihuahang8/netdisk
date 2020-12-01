package com.micro.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.Share;
import com.micro.disk.bean.ShareBean;
import com.micro.disk.service.ShareService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.mvc.UserInfoUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/disk/sharebyself")
public class ShareMySelf {
    @Reference(check=false)
    private ShareService shareService;

    /**
     * 我的分享
     * @param pi
     * @param type
     * @param status
     * @param request
     * @return
     */
    @PostMapping("/findList")
    public PageInfo<Share> findList(com.micro.disk.bean.PageInfo<ShareBean> pi, Integer type, Integer status, HttpServletRequest request){
        SessionUserBean user= UserInfoUtils.getBean(request);
        return shareService.findMyShare(pi.getPage(), pi.getLimit(), user.getId(),type,status);
    }

    /**
     * 查找某个分享下的文件
     * @param id
     * @param pid
     * @return
     */
    @PostMapping("/findShareList")
    public Result findShareList(String id, String pid){
        try{
            return ResultUtils.success("查询成功",shareService.findShareFileListBySelf(id, pid));
        }catch(Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }

    /**
     * 取消分享
     * @param idjson
     * @return
     */
    @PostMapping("/cancelShare")
    public Result cancelShare(String idjson){
        try{
            if(StringUtils.isEmpty(idjson)){
                throw new RuntimeException("请选择取消分享的记录");
            }
            JsonUtils jsonUtils=new JsonJackUtils();
            List<Map> lists=jsonUtils.jsonToList(idjson, Map.class);
            List<String> ids=new ArrayList<String>();

            if(!CollectionUtils.isEmpty(lists)){
                for(Map m:lists){
                    ids.add(m.get("id")==null?"":m.get("id").toString());
                }
            }
            shareService.cancelShare(ids);
            return ResultUtils.success("取消分享成功",null);
        }catch (Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }

    /**
     * 查看某个分享的好友集合
     * @param shareid
     * @return
     */
    @PostMapping("/findFriends")
    public Result findFriends(String shareid){
        try{
            return ResultUtils.success("查找成功",shareService.findFriends(shareid));
        }catch (Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }

}
