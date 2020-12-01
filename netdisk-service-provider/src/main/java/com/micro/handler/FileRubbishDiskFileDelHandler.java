package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskFileDelDao;
import com.micro.modeltree.DiskFileRubbishTree;
import com.micro.param.FileRubbishDelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileRubbishDiskFileDelHandler extends Handler {
    @Autowired
    private DiskFileDelDao diskFileDelDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileRubbishDelRequest){
            FileRubbishDelRequest bean = (FileRubbishDelRequest) request;
            List<DiskFileRubbishTree> dfrts = bean.getDiskFileRubbishTrees();
            List<String> rubbishIds = new ArrayList<>();
            //递归删除
            diDelete(dfrts,rubbishIds);
            bean.setRubbishIds(rubbishIds);
            this.updateRequest(bean);
        }
    }
    private void diDelete(List<DiskFileRubbishTree> dfrts,List<String> rubbishIds){
        for(DiskFileRubbishTree dfrt:dfrts){
            //1.去数据库删除
            diskFileDelDao.delete(dfrt.getId());
            //2.添加到集合
            rubbishIds.add(dfrt.getId());
            //3.递归
            diDelete(dfrt.getChildren(),rubbishIds);
        }
    }
}
