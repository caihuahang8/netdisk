package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.MergeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MergeSpecialDealHandler extends Handler {
    @Autowired
    private FiledealContext filedealContext;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof MergeRequest){
            MergeRequest bean=(MergeRequest) request;

            //filemd5在disk_md5和disk_file都不存在，则需要裁剪（图片、视频）
            if(bean.isExistindiskmd5()==false&&bean.isExistindiskfile()==false){
                filedealContext.deal(bean.getTypecode(), bean.getDiskfileid(),bean.getFilemd5(), bean.getTemps());
            }
        }else{
            throw new RuntimeException("MergeSpecialDealHandler==参数不对");
        }
    }
}
