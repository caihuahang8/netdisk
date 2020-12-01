package com.micro.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.CookieUtils;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.disk.bean.FolderPropertyBean;
import com.micro.disk.bean.ShareFriendsBean;
import com.micro.disk.service.FileService;
import com.micro.disk.service.ShareService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.service.UserService;
import com.micro.mvc.UserInfoUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/disk/filecommon")
@RestController
public class FileCommonController {
    @Reference(check = false)
    FileService fileService;
    @Reference(check = false)
    ShareService shareService;
    @Reference(check = false)
    UserService userService;

    private final JsonJackUtils jsonJackUtils = new JsonJackUtils();

    /**
     * 删除文件
     * @param request
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    public Result delete(HttpServletRequest request, String ids){
        try{
            SessionUserBean sessionUserBean  =  UserInfoUtils.getBean(request);
            if(StringUtils.isEmpty(ids)){
                throw new RuntimeException("ids不能为空");
            }
            List<Map> idMs = (List<Map>) jsonJackUtils.jsonToList(ids, Map.class);
            List<String> idsList = new ArrayList<String>();
            for (Map map:idMs){
                idsList.add(map.get("id").toString());
            }
            fileService.delete(sessionUserBean.getId(),sessionUserBean.getNickname(),idsList);
            return ResultUtils.success("删除成功",null);
        }catch (Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }

    /**
     * 文件重命名
     * @param id
     * @param filename
     * @param request
     * @return
     */
    @PostMapping("/rename")
    public Result rename(String id ,String filename,HttpServletRequest request){
        try{
            //1.获得用户信息
            SessionUserBean sessionUserBean = UserInfoUtils.getBean(request);
            if (sessionUserBean==null){
                throw new RuntimeException("用户没有登陆");
            }
            //2.重命名
            fileService.rename(id,filename,sessionUserBean.getId());
            return ResultUtils.success("重命名成功",null);
        }catch (Exception e ){
            return ResultUtils.error(e.getMessage());
        }
    }

    /**
     * 查找文件
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/findOne")
    public Result findOne(String id,HttpServletRequest request){
        try{
            SessionUserBean sessionUserBean  =  UserInfoUtils.getBean(request);
            if (sessionUserBean==null){
                throw new RuntimeException("用户没有登陆");
            }
            return ResultUtils.success("查找成功",fileService.findOne(id));
        }catch (Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }

    /**
     * 查找文件夹属性
     * @param id 文件夹id（pid）
     * @return
     */
    @PostMapping("/findFolderProp")
    public Result findFolderProperty(String id,HttpServletRequest request){
        try{
            SessionUserBean sessionUserBean = UserInfoUtils.getBean(request);
            if(sessionUserBean==null){
                throw new RuntimeException("用户没有登陆");
            }
            //pid userid
            FolderPropertyBean folderPropertyBean = fileService.findFolderProperty(id,sessionUserBean.getId());
            return ResultUtils.success("查找文件夹属性成功",folderPropertyBean);
        }catch (Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }

    /**
     * 分享秘链
     * @param idjson
     * @param title
     * @param sharetype
     * @param effect
     * @param type
     * @param request
     * @return
     */
    @PostMapping("/shareSecret")
    public Result shareSecret(String idjson,String title,Integer sharetype,Integer effect,Integer type,HttpServletRequest request){
        try{
            //json转换
            List<String> ids=new ArrayList<String>();
            if(!org.springframework.util.StringUtils.isEmpty(idjson)){
                JsonUtils jsonUtils=new JsonJackUtils();
                List<Map> lists=jsonUtils.jsonToList(idjson, Map.class);
                lists.forEach(m->{
                    ids.add(m.get("id")==null?"":m.get("id").toString());
                });
            }

            SessionUserBean user=UserInfoUtils.getBean(request);
            String userid=user.getId();
            String username=user.getNickname();
            return ResultUtils.success("分享成功",shareService.shareSecret(ids,title, userid, username, sharetype, effect,type));
        }catch(Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }

    /**
     * 好友分享
     * @param idjson
     * @param title
     * @param userJson
     * @param type
     * @param request
     * @return
     */
    @PostMapping("/shareFriends")
    public Result shareFriends(String idjson,String title,String userJson,Integer type,HttpServletRequest request){
        try{
            //json转换
            List<String> ids=new ArrayList<String>();
            if(!org.springframework.util.StringUtils.isEmpty(idjson)){
                JsonUtils jsonUtils=new JsonJackUtils();
                List<Map> lists=jsonUtils.jsonToList(idjson, Map.class);
                lists.forEach(m->{
                    ids.add(m.get("id")==null?"":m.get("id").toString());
                });
            }

            List<ShareFriendsBean> friends=new ArrayList<>();
            if(!org.springframework.util.StringUtils.isEmpty(userJson)){
                JsonUtils jsonUtils=new JsonJackUtils();
                friends=jsonUtils.jsonToList(userJson, ShareFriendsBean.class);
            }

            SessionUserBean user=UserInfoUtils.getBean(request);
            String userid=user.getId();
            String username=user.getNickname();


            shareService.shareFriends(ids,friends,title, userid, username,type);
            return ResultUtils.success("分享成功",null);
        }catch(Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }
    @RequestMapping("/addFolder")
    public Result addFolder(String name,String pid,HttpServletRequest request){
        try{
            SessionUserBean sessionUserBean = UserInfoUtils.getBean(request);
            String username = sessionUserBean.getNickname();
            String userid = sessionUserBean.getId();
            fileService.addFolder(name,userid,username,pid);
            return ResultUtils.success("创建成功",null);
        }catch (RuntimeException e){
            return ResultUtils.error("创建错误");
        }
    }
}
