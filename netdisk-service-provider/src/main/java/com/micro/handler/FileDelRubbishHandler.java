package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskFileDao;
import com.micro.db.dao.DiskFileDelDao;
import com.micro.model.DiskFileDel;
import com.micro.modeltree.DiskFileTree;
import com.micro.param.FileDelRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Component
public class FileDelRubbishHandler extends Handler {
    @Autowired
    DiskFileDao diskFileDao;
    @Autowired
    DiskFileDelDao diskFileDelDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileDelRequest){
            FileDelRequest bean = (FileDelRequest) request;
            List<DiskFileTree> dfts = bean.getFiles();
            List<String> rubbishs = new ArrayList<>();
            addDelRubbish(rubbishs,"0",dfts);
            bean.setRubbishs(rubbishs);
            this.updateRequest(bean);
        }else {
            throw new RuntimeException("FileDelRubbishHandler参数错误");
        }
    }
    private void addDelRubbish(List<String> rubbishs,String pid,List<DiskFileTree> dfts){
           // 递归添加删除文件到回收站（Disk_file_del）,去Disk_file删除文件
        try{
            if (!CollectionUtils.isEmpty(dfts)) {
                for (DiskFileTree dft : dfts) {
                    //1.先添加到回收站
                    DiskFileDel diskFileDel = new DiskFileDel();
                    BeanUtils.copyProperties(dft, diskFileDel);
                    diskFileDel.setId(null);
                    diskFileDel.setDeletetime(new Date());
                    diskFileDel.setPid(pid);
                    diskFileDelDao.save(diskFileDel);
                    rubbishs.add(diskFileDel.getId());

                    //2.去disk_file删除
                    diskFileDao.delete(dft.getId());


                    addDelRubbish(rubbishs, dft.getId(), dft.getChildren());
                }
            }
        }catch (Exception e){
            throw new RuntimeException();
        }
    }
}
