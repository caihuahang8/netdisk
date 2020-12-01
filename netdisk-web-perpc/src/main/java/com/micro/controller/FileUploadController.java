package com.micro.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.disk.bean.Chunk;
import com.micro.disk.bean.MergeFileBean;
import com.micro.disk.service.FileService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.service.UserService;
import com.micro.mvc.UserInfoUtils;
import com.micro.pojo.ChunkPojo;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/disk/fileupload")
public class FileUploadController {

    @Reference(check = false)
    private FileService fileService;
    @Reference(check = false)
    private UserService userService;


    @RequestMapping("/uploadChunk")
    public Result uploadChunk(MultipartFile file, ChunkPojo chunkPojo, String token, HttpServletRequest request,
                              HttpServletResponse response){
        try {
            //1.查找用户信息
            SessionUserBean user = userService.getUserByToken(token);
            if (user==null){
                throw new RuntimeException("token无效");
            }else {
                request.setAttribute("userid", user.getId());
                request.setAttribute("username",user.getNickname());
            }
            //2.上传文件
            if (file == null){
                throw new RuntimeException("切块不能为空");
            }

            //3.参数
            Chunk chunk=new Chunk();
            BeanUtils.copyProperties(chunkPojo, chunk);
            chunk.setUserid(user.getId());
            chunk.setUsername(user.getNickname());
            chunk.setBytes(file.getBytes());

            //4.调用切块上传接口
            fileService.uploadChunk(chunk);

            return ResultUtils.success("上传切块成功", null);
        }catch (Exception e){
            e.printStackTrace();
            response.setStatus(500);
            throw new RuntimeException(e.getMessage());
        }
    }
    @RequestMapping("/mergeChunk")
    public Result mergeChunk(MergeFileBean bean, HttpServletRequest request) {
        try {
            SessionUserBean user = UserInfoUtils.getBean(request);
            bean.setUserid(user.getId());
            bean.setUsername(user.getNickname());
            fileService.mergeChunk(bean);
            return ResultUtils.success("合并切块成功", null);
        } catch (Exception e) {
            return ResultUtils.error(e.getMessage());
        }
    }
    @RequestMapping("/checkFile")
    public Result checkFile(String filemd5) {
        try {
            return ResultUtils.success("查询成功", fileService.checkFile(filemd5));
        } catch (Exception e) {
            return ResultUtils.error(e.getMessage());
        }
    }
}
