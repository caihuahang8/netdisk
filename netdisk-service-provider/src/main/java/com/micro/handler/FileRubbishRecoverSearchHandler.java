package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskFileDelDao;
import com.micro.model.DiskFileDel;
import com.micro.modeltree.DiskFileRubbishTree;
import com.micro.param.FileRubbishRecoverRequest;
import com.micro.utils.CapacityBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileRubbishRecoverSearchHandler extends Handler {

    @Autowired
    DiskFileDelDao diskFileDelDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof FileRubbishRecoverRequest ){
            FileRubbishRecoverRequest bean = (FileRubbishRecoverRequest) request;
            CapacityBean capacityBean = new CapacityBean(0L);
            List<String> ids = bean.getIds();
            List<DiskFileRubbishTree> files = new ArrayList<>();
            for(int i = 0 ; i<ids.size();i++){
                DiskFileDel diskFileDel = diskFileDelDao.findOne(ids.get(i));
                int index = i + 1 ;
                if (diskFileDel==null){
                    throw new RuntimeException("第"+index+"文件不存在");
                }
                DiskFileRubbishTree dfrt = converBean(diskFileDel);
                capacityBean.setTotalsize(capacityBean.getTotalsize()+diskFileDel.getFilesize());
                List<DiskFileRubbishTree> children = new ArrayList<>();
                diSearch(bean.getUserid(),diskFileDel.getId(),capacityBean,children);
                dfrt.setChildren(children);
                files.add(dfrt);
            }
            bean.setChildren(files);
            bean.setCapacityBean(capacityBean);
            this.updateRequest(bean);
        }else {
            throw new RuntimeException("FileRubbishRecoverSearchHandler参数问题");
        }

    }
    private void diSearch(String userid,String pid,CapacityBean capacityBean,List<DiskFileRubbishTree> files){
        List<DiskFileDel> dfds =  diskFileDelDao.findListByPid(userid,pid);
        if (!CollectionUtils.isEmpty(dfds)){
            for (DiskFileDel del : dfds){
                DiskFileRubbishTree dfrt = converBean(del);
                capacityBean.setTotalsize(capacityBean.getTotalsize()+del.getFilesize());
                List<DiskFileRubbishTree> children = new ArrayList<>();
                diSearch(userid,pid,capacityBean,children);
                dfrt.setChildren(children);
                files.add(dfrt);
            }
        }
    }
    public DiskFileRubbishTree converBean(DiskFileDel del){
        DiskFileRubbishTree tree=new DiskFileRubbishTree();
        tree.setId(del.getId());
        tree.setPid(del.getPid());
        tree.setFilename(del.getFilename());
        tree.setFilesize(del.getFilesize());
        tree.setFilesuffix(del.getFilesuffix());
        tree.setTypecode(del.getTypecode());
        tree.setFilemd5(del.getFilemd5());
        tree.setFiletype(del.getFiletype());
        tree.setCreateuserid(del.getCreateuserid());
        tree.setCreateusername(del.getCreateusername());
        tree.setCreatetime(del.getCreatetime());
        tree.setDeletetime(del.getDeletetime());
        tree.setThumbnailurl(del.getThumbnailurl());
        tree.setImgsize(del.getImgsize());
        return tree;
    }
}
