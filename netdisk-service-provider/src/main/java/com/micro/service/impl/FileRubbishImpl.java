package com.micro.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.micro.core.Bootstrap;
import com.micro.core.HandlerInitializer;
import com.micro.core.Pipeline;
import com.micro.db.dao.DiskFileDelDao;
import com.micro.db.jdbc.DiskFileRubbishJdbc;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.RubbishBean;
import com.micro.disk.service.FileRubbishService;
import com.micro.handler.*;
import com.micro.param.FileRubbishDelRequest;
import com.micro.param.FileRubbishRecoverRequest;
import com.micro.utils.SpringContentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass= FileRubbishService.class,timeout=120000) //Dubbo注解
@Component //Spring注解
@Transactional
public class FileRubbishImpl implements FileRubbishService {
    @Autowired
    DiskFileRubbishJdbc diskFileRubbishJdbc;

    @Autowired
    DiskFileDelDao diskFileDelDao;
    @Autowired
    SpringContentUtils springContentUtils;
    //userid page limit
    @Override
    public PageInfo<RubbishBean> findRubbish(String userid,Integer page,Integer limit){
       return  diskFileRubbishJdbc.findRubbishList(page,limit,userid,null,null);
    }


    @Override
    public void deleteRubbish(List<String> ids,String userid){
        FileRubbishDelRequest fileRubbishDelRequest = new FileRubbishDelRequest();
        fileRubbishDelRequest.setIds(ids);
        fileRubbishDelRequest.setUserid(userid);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.childHandler(new HandlerInitializer(fileRubbishDelRequest,null) {
            @Override
            protected void initChannel(Pipeline pipeline) {
                //1.参数校验
                pipeline.addLast(springContentUtils.getHandler(FileRubbishValidatorHandler.class));
                //2.递归保存所有删除信息
                pipeline.addLast(springContentUtils.getHandler(FileRubbishSearchHandler.class));
                //3.删除disk_file_del
                pipeline.addLast(springContentUtils.getHandler(FileRubbishDiskFileDelHandler.class));
                //4.Redis监听删除
                pipeline.addLast(springContentUtils.getHandler(FileRubbishDelRedisHandler.class));
            }
        });
        bootstrap.execute();
    }

    @Override
    public void recover(String folderid, List<String> ids, String username, String userid) {
        FileRubbishRecoverRequest request = new FileRubbishRecoverRequest();
        request.setCreateusername(username);
        request.setIds(ids);
        request.setFolderid(folderid);
        request.setUserid(userid);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.childHandler(new HandlerInitializer(request,null) {
            @Override
            protected void initChannel(Pipeline pipeline) {
                //1.基本参数校验
                pipeline.addLast(springContentUtils.getHandler(FileRubbishRecoverValidatorHandler.class));
                //2.收集被删除的信息
                pipeline.addLast(springContentUtils.getHandler(FileRubbishRecoverSearchHandler.class));
                //3.检查容量
                pipeline.addLast(springContentUtils.getHandler(FileRubbishRecoverCapacityHandler.class));
                //4.重新保存到disk_fiel
                pipeline.addLast(springContentUtils.getHandler(FileRubbishRecoverSavaDiskFileHandler.class));
                //5.删除redis
                pipeline.addLast(springContentUtils.getHandler(FileRubbishRecoverRedisHandler.class));
                //6.更新容量
                pipeline.addLast(springContentUtils.getHandler(FileRubbishRecoverUpdateCapacityHandler.class));
            }
        });
    }
}
