package com.micro.handler;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.common.DateUtils;
import com.micro.common.RandomUtils;
import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskShareDao;
import com.micro.model.DiskShare;
import com.micro.param.ShareSecretRequest;
import com.micro.param.ShareSecretResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ShareSaveDiskShareHandler extends Handler {
    @NacosValue(value="${vueprojecthost}",autoRefreshed=true)
    private String vueprojecthost;
    @Autowired
    private DiskShareDao diskShareDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof ShareSecretRequest){
            ShareSecretRequest bean = (ShareSecretRequest) request;
            String code="";
            Date endtime  = null;
            if (bean.getSharetype()==0){
                //私密链接 4位数密码
                 code = RandomUtils.getRandom(4);
            }
            if (bean.getEffect()!=0){
                endtime=DateUtils.getSpecialDate(bean.getEffect());
            }
            DiskShare diskShare = new DiskShare();
            diskShare.setShareusername(bean.getUsername());
            diskShare.setShareuserid(bean.getUserid());
            diskShare.setSharetype(bean.getSharetype());
            diskShare.setEndtime(endtime);
            diskShare.setEffect(bean.getEffect());
            diskShare.setTitle(bean.getTitle());
            diskShare.setCode(code);
            diskShare.setUrl("");
            diskShare.setType(bean.getType());
            diskShare.setStatus(0);
            diskShare.setSavecount(0);
            diskShare.setSharetime(new Date());
            diskShareDao.save(diskShare);

            String shareid = diskShare.getId();
            String url = vueprojecthost+"/#/sharepwd/"+shareid.toUpperCase();
            diskShare.setUrl(url);
            diskShareDao.save(diskShare);
            bean.setShareid(shareid);
            this.updateRequest(bean);

            ShareSecretResponse res = new ShareSecretResponse();
            res.setCode(code);
            res.setUrl(url);
            this.updateResponse(res);
        }else{
            throw new RuntimeException("ShareSecretSaveHandler==参数不对");
        }
    }
}
