package com.micro.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.disk.service.FileService;
import com.micro.disk.service.ShareService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.mvc.UserInfoUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/disk/shareextract")
public class ShareCommonController {
    @Reference(check = false)
    private ShareService shareService;
    @Reference(check = false)
    private FileService fileService;
    private final JsonUtils jsonUtils = new JsonJackUtils();
    /**
     * 查找分享文件信息
     * @param id
     * @return
     */
    @PostMapping("/findShareInfo")
    public Result findShareInfo(String id){
        try{
            return ResultUtils.success("查找成功",shareService.findShareInfo(id));
        }catch (Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }

    /**
     * 验证id
     * @param id
     * @param code
     * @return
     */
    @PostMapping("/validateCode")
    public Result validateCode(String id,String code){
        try{
            String token=shareService.validateCode(id, code);
            return ResultUtils.success("验证成功",token);
        }catch(Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }

    /**
     * 文件分享集合
     * @param id
     * @param pid
     * @param token
     * @return
     */
    @PostMapping("/findShareList")
    public Result findShareList(String id,String pid,String token){
        try {
            return ResultUtils.success("查找成功",shareService.findShareFileListFromSecret(id,pid,token));
        }catch (Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }

    /**
     * 分享文件转存
     * @param shareid
     * @param folderid
     * @param idjson
     * @param request
     * @return
     */
    @PostMapping("/saveFromShare")
    public Result saveFromShare(String shareid, String folderid, String idjson, HttpServletRequest request){
        try{
            if(idjson==null){
                throw new RuntimeException("勾选的文件id不存在");
            }
            List<String> ids = new ArrayList();
            final List<Map> list = jsonUtils.jsonToList(idjson, Map.class);
            list.forEach(map->{
                ids.add(map.get("id")==null?"":map.get("id").toString());
            });
            SessionUserBean sessionUserBean = UserInfoUtils.getBean(request);
            String userid = sessionUserBean.getId();
            String username = sessionUserBean.getNickname();
            fileService.saveFromShare(userid,username,folderid,shareid,ids);
            return ResultUtils.success("转存成功",null);
        }catch (Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }




}
