package com.micro.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.disk.bean.FileBean;
import com.micro.disk.service.FilePreviewService;
import com.micro.disk.service.FileService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.service.UserService;
import com.micro.mvc.UserInfoUtils;
import com.micro.store.utils.BandwidthLimiter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;


@RestController
@RequestMapping("/disk/filedownload")
public class FileDownloadController {
    @Reference(check = false)
    private FileService fileService;
    @Reference(check = false)
    private UserService userService;
    @Reference(check = false)
    private FilePreviewService filePreviewService;
    @GetMapping("/download")
    public void download(String fileid, HttpServletRequest request, HttpServletResponse response){
        try {

            FileBean fileBean = fileService.findOne(fileid);
            if (fileBean==null){
                throw new RuntimeException("文件Id不存在");
            }

            String fileMd5 = fileBean.getFilemd5();
            String userId = fileBean.getCreateuserid();
            String filename = fileBean.getFilename();
            SessionUserBean sessionUserBean=UserInfoUtils.getBean(request);
            if (!sessionUserBean.getId().equals(userId)){
                throw new RuntimeException("您不是文件的拥有者,无法下载");
            }

            //2.设置响应头
            String userAgent = request.getHeader("User-Agent");
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {// 针对IE或者以IE为内核的浏览器：
                filename = java.net.URLEncoder.encode(filename, "UTF-8");
            } else {// 非IE浏览器的处理：
                filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
            }
            response.setHeader("content-disposition", "attachment;filename=" + filename);
            // response.setContentType("application/octet-stream");

            OutputStream out = response.getOutputStream();
            List<String>  urls = filePreviewService.getChunksByFilemd5(fileMd5);
            if (urls.size()==0){
                throw new RuntimeException("无法根据MD5查找切块存取路径集合");
            }
            BandwidthLimiter bandwidthLimiter =  new BandwidthLimiter();
            if(sessionUserBean.getToken().equals("token-vip")) {
                 bandwidthLimiter.setMaxRate(1024);
            }
            //下载分块
             for(String url : urls){
                byte[] bytes =  filePreviewService.getBytesByUrl(url);
                //限流下载
                bandwidthLimiter.limitNextBytes(bytes.length);
                out.write(bytes);
                out.flush();
             }
             out.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 多文件下载（需要VIP）
     */
}
