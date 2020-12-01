package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskFileDelDao;
import com.micro.model.DiskFileDel;
import com.micro.modeltree.DiskFileRubbishTree;
import com.micro.param.FileRubbishDelRequest;
import com.micro.utils.CapacityBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileRubbishSearchHandler extends Handler {
    @Autowired
    private DiskFileDelDao diskFileDelDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof FileRubbishDelRequest){
          FileRubbishDelRequest bean = new FileRubbishDelRequest();
            //递归收集所有需要删除文件的树形结构
            List<DiskFileRubbishTree> dfrts = new ArrayList<>();
            List<String> ids = bean.getIds();
            for (int i=0;i<ids.size();i++){
                DiskFileDel dfd =  diskFileDelDao.findOne(ids.get(0));
                int index = i+1;
                if(dfd==null){
                    throw new RuntimeException("选中的第"+index+"文件不存在");
                }
                DiskFileRubbishTree dfrt = new DiskFileRubbishTree();
                BeanUtils.copyProperties(dfd,dfrt);
                List<DiskFileRubbishTree> children = new ArrayList<>();
                diSearchRubbish(bean.getUserid(),"0",children);
                dfrt.setChildren(children);
                dfrts.add(dfrt);
            }
            bean.setDiskFileRubbishTrees(dfrts);
        }else {
            throw new RuntimeException("FileRubbishIsExistHandler参数问题");
        }
    }
    private void diSearchRubbish(String userid,String pid,List<DiskFileRubbishTree> files){
        List<DiskFileDel> dfds =   diskFileDelDao.findListByPid(userid,pid);
        if (!CollectionUtils.isEmpty(dfds)){
            for (DiskFileDel dfd:dfds){
                 DiskFileRubbishTree dftt = new DiskFileRubbishTree();
                 BeanUtils.copyProperties(dfd,dftt);
                 List<DiskFileRubbishTree> children = new ArrayList<>();
                 diSearchRubbish(userid,dfd.getId(),children);
                 dftt.setChildren(children);
                 files.add(dftt);
            }
        }
    }
}
