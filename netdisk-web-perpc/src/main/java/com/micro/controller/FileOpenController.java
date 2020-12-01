package com.micro.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.PreviewContext;
import com.micro.common.MimeUtils;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.disk.bean.FileBean;
import com.micro.disk.service.FilePreviewService;
import com.micro.disk.service.FileService;
import com.micro.disk.service.TypeSuffixService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.service.UserService;
import com.micro.mvc.UserInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/disk/fileopen")
public class FileOpenController {
    @Reference(check = false)
    FileService fileService;
    @Reference(check = false)
    UserService userService;
    @Reference(check = false)
    FilePreviewService filePreviewService;

    @Autowired
    private PreviewContext previewContext;

    @Reference(check = false)
    private TypeSuffixService typeSuffixService;

    /**
     * 在线预览-在线编辑
     * @param fileid
     * @param code
     * @param token
     * @return
     */
    @RequestMapping("/writeTextStr")
    public Result writeTextStr(String fileid,String code,String token,HttpServletRequest request){
        SessionUserBean sessionUserBean = UserInfoUtils.getBean(request);
        if (sessionUserBean==null){
            return ResultUtils.error("账号没有登陆");
        }
        try{
            //1.根据文件id，去disk_file表里查询
            FileBean fileBean =  fileService.findOne(fileid);
            //2.根据文件md5，去disk_chunks里查找路径下载
            String filemd5 = fileBean.getFilemd5();
            if (!sessionUserBean.getId().equals(fileBean.getCreateuserid())){
                throw new RuntimeException("查找不到文件");
            }
            String data=getText(filemd5, code);
            return ResultUtils.success("查询成功", data);
        }catch (Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }

    private String getText(String filemd5,String code) throws UnsupportedEncodingException {
        List<String> urls=filePreviewService.getChunksByFilemd5(filemd5);
        List<byte[]> arrs=new ArrayList<byte[]>();
        int current=0;
        int totallen=0;
        for(String url:urls){
            byte[] bytes=filePreviewService.getBytesByUrl(url);
            arrs.add(bytes);
            totallen=totallen+bytes.length;
        }
        byte[] resultbytes=new byte[totallen];
        for(int i=0;i<arrs.size();i++){
            byte[] bytes=arrs.get(i);
            System.arraycopy(bytes, 0, resultbytes, current, bytes.length);
            current=current+bytes.length;
        }
        if(StringUtils.isEmpty(code)){
            code="UTF-8";
        }
        String data=new String(resultbytes, code);
        return data;
    }
    @GetMapping("/writeByte")
    public void writeByte(String fileid, String code, HttpServletRequest request, HttpServletResponse response){
        try{
            FileBean fb=fileService.findOne(fileid);
            if(fb==null){
                throw new RuntimeException("文件ID不存在");
            }
            String filemd5=fb.getFilemd5();
            String filename=fb.getFilename();
            SessionUserBean user= UserInfoUtils.getBean(request);
            if(!user.getId().equals(fb.getCreateuserid())){
                throw new RuntimeException("您不是文件的拥有者,无法获取文件");
            }

            if(StringUtils.isEmpty(code)){
                code="UTF-8";
            }
            String mime= MimeUtils.getMime(fb.getFilesuffix());
            response.setHeader("Content-Type", ""+mime+";charset="+code);
            response.setHeader("Content-Disposition", "inline;filename="+filename);

            //Office文件对应的格式
            String[] types={"doc","docx","xls","xlsx","ppt","pptx"};
            boolean isoffice=false;
            for(String type:types){
                if(type.equals(fb.getFilesuffix())){
                    isoffice=true;
                    break;
                }
            }
            if(isoffice){
                officeFile(filemd5, filename, response);
            }else{
                commonFile(filemd5,response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void officeFile(String filemd5,String filename,HttpServletResponse response){
        File file = null;
        try{
            //1.获取所有切块的url
            List<String> urls = filePreviewService.getChunksByFilemd5(filemd5);
            //2.把切块写到一起合成完成的文件
            List<byte[]> arrs = new ArrayList<>();
            int current = 0;
            int totallen = 0;
            for (String url:urls){
                byte[] bytes=filePreviewService.getBytesByUrl(url);
                arrs.add(bytes);
                totallen=totallen+bytes.length;
            }
            byte[] resultbytes = new byte[totallen];
            for (int i = 0 ;i<arrs.size();i++){
                byte[] bytes = arrs.get(i);
                System.arraycopy(bytes,0,resultbytes,current,bytes.length);
                current=current+bytes.length;
            }

            //3.pdf字节数组
            byte[] pdfbytes = previewContext.converToPdf(resultbytes);

            OutputStream out = response.getOutputStream();
            out.write(pdfbytes);
            out.close();
        }catch (Exception E){
            E.printStackTrace();
        }finally {
            if(file!=null){
                file.delete();
            }
        }
        }
    private void commonFile(String filemd5,HttpServletResponse response) throws IOException {
        List<String> urls=filePreviewService.getChunksByFilemd5(filemd5);
        OutputStream out=response.getOutputStream();
        for(String url:urls){
            byte[] bytes=filePreviewService.getBytesByUrl(url);
            out.write(bytes);
            out.flush();
        }
        out.close();
    }

    /**
     * 内容对比
     * @param filemd5
     * @param code
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/writeTextStrByMd5")
    public Result writeTextStrByMd5(String filemd5,String code,HttpServletRequest request,HttpServletResponse response){
        try{
            String data=getText(filemd5, code);
            return ResultUtils.success("查询成功", data);
        }catch(Exception e){
            e.printStackTrace();
            return ResultUtils.error(e.getMessage());
        }
    }

    @PostMapping("/findComponentsBySuffix")
    public Result findComponentsBySuffix(String filesuffix){
        try{
            return ResultUtils.success("查询成功", typeSuffixService.findComponentsBySuffix(filesuffix));
        }catch(Exception e){
            return ResultUtils.error(e.getMessage());
        }
    }
}
