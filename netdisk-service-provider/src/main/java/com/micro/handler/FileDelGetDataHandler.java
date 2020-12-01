package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskFileDao;
import com.micro.model.DiskFile;
import com.micro.modeltree.DiskFileTree;
import com.micro.param.FileDelRequest;
import com.micro.utils.CapacityBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileDelGetDataHandler extends Handler {
    @Autowired
    private DiskFileDao diskFileDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileDelRequest){
            FileDelRequest bean = (FileDelRequest) request;
            List<String> ids =  bean.getIds();
            CapacityBean capacityBean = new CapacityBean(0L);
            List<DiskFileTree> files = new ArrayList<>();

            for (int i = 0 ;i<ids.size();i++){
                int index = i+1;
                String id = ids.get(i);
                DiskFile diskFile = diskFileDao.findOne(ids.get(i));
                if(diskFile==null){
                    throw new RuntimeException("选中的第"+index+"个文件(ID="+id+")不存在");
                }
                DiskFileTree tree = new DiskFileTree();
                BeanUtils.copyProperties(diskFile,tree);
                List<DiskFileTree> children = new ArrayList<>();
                diDelSearch(diskFile.getCreateuserid(),diskFile.getId(),children,capacityBean);

                tree.setChildren(children);
                files.add(tree);
            }
            bean.setFiles(files);
            this.updateRequest(bean);
        }
    }
    public void diDelSearch(String userid,String pid,List<DiskFileTree> files,CapacityBean capacityBean){
        List<DiskFile> dfs =   diskFileDao.findListByPid(userid,pid);
        if (!CollectionUtils.isEmpty(dfs)){
            for(DiskFile df : dfs){
                DiskFileTree tree = new DiskFileTree();
                capacityBean.setTotalsize(df.getFilesize()+capacityBean.getTotalsize());
                BeanUtils.copyProperties(df,tree);
                List<DiskFileTree> children = new ArrayList<>();
                diDelSearch(df.getCreateuserid(),df.getId(),children,capacityBean);
                tree.setChildren(children);
                files.add(tree);
            }
        }
    }
}
