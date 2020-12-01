package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskFileDao;
import com.micro.db.dao.DiskShareFileDao;
import com.micro.model.DiskFile;
import com.micro.model.DiskShareFile;
import com.micro.param.ShareSecretRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class ShareSaveDiskShareFileHandler extends Handler {
    @Autowired
    DiskShareFileDao diskShareFileDao;
    @Autowired
    DiskFileDao diskFileDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof ShareSecretRequest){
            ShareSecretRequest bean = new ShareSecretRequest();
            if(bean.getType()==0){
                List<String> ids=bean.getIds();

                for(String id:ids){
                    DiskFile file=diskFileDao.findOne(id);
                    if(file==null){
                        throw new RuntimeException("分享的记录不存在,id="+id);
                    }
                    DiskShareFile shareFile=new DiskShareFile();
                    BeanUtils.copyProperties(file, shareFile);
                    shareFile.setId(null);
                    shareFile.setShareid(bean.getShareid());
                    shareFile.setPid("0");
                    diskShareFileDao.save(shareFile);
                    dgFile(bean.getUserid(),id,shareFile.getId() ,bean.getShareid());
                }
            }
        }else{
            throw new RuntimeException("ShareSecretDetailFromFileHandler==参数不对");
        }
    }
    private void dgFile(String userid,String pid,String sharefileid,String shareid){
        List<DiskFile> dfs = diskFileDao.findListByPid(userid,pid);
        if (!CollectionUtils.isEmpty(dfs)){
            for (DiskFile df:dfs){
                DiskShareFile dsf = new DiskShareFile();
                BeanUtils.copyProperties(df,dsf);
                dsf.setShareid(shareid);
                dsf.setPid(sharefileid);
                dsf.setId(null);
                diskShareFileDao.save(dsf);
                dgFile(userid,df.getId(),dsf.getId(),shareid);
            }
        }
    }
}
