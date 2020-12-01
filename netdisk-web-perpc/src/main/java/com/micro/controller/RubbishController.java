package com.micro.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.common.json.JsonJackUtils;
import com.micro.disk.service.FileRubbishService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.mvc.UserInfoUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/disk/rubbish")
public class RubbishController {
    @Reference(check = false)
    FileRubbishService fileRubbishService;

    private final JsonJackUtils jsonJackUtils = new JsonJackUtils();

    @RequestMapping("/findList")
    public Result findList(Integer page, Integer limit, HttpServletRequest request){
        try{
            SessionUserBean sessionUserBean  =  UserInfoUtils.getBean(request);
            if(sessionUserBean==null){
                throw new RuntimeException("用户没有登陆");
            }
            return ResultUtils.success("查找成功",fileRubbishService.findRubbish(sessionUserBean.getId(),page,limit));
        }catch (Exception e){
            return   ResultUtils.error(e.getMessage());
        }
    }

    @PostMapping("/delete")
    public Result delete(String idjson,HttpServletRequest request){
        try{
            SessionUserBean sessionUserBean = UserInfoUtils.getBean(request);
            if(sessionUserBean==null){
                throw new RuntimeException("用户没有登陆");
            }
            List<String> ids = new ArrayList<String>();
            List<Map> maps = jsonJackUtils.jsonToList(idjson, Map.class);
            for (Map map:maps){
                ids.add(map.get("id").toString());
            }
            fileRubbishService.deleteRubbish(ids,sessionUserBean.getId());
            return ResultUtils.success("查找成功",null);
        }catch (Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }
    @PostMapping("/recover")
    public Result recover(String folderid,String idjson,HttpServletRequest request){
        try{
            SessionUserBean sessionUserBean = UserInfoUtils.getBean(request);
            if(sessionUserBean==null){
                throw new RuntimeException("用户没有登陆");
            }
            List<String> ids = new ArrayList<String>();
            List<Map> maps = jsonJackUtils.jsonToList(idjson,Map.class);
            for (Map map:maps){
                ids.add(map.get("id").toString());
            }
            //folderid ids username userid
            fileRubbishService.recover(folderid,ids,sessionUserBean.getNickname(),sessionUserBean.getId());
            return ResultUtils.success("还原成功",null);
        }catch (Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }
}
