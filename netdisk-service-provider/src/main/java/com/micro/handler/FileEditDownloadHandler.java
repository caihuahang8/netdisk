package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.FileEditRequest;
import com.micro.store.context.StoreContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileEditDownloadHandler extends Handler {
    @Autowired
    StoreContext storeContext;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof FileEditRequest){
            FileEditRequest bean = (FileEditRequest) request;
            String folderpath  =  storeContext.upload(null,bean.getBytes(),bean.getFilename());
            bean.setFolderpath(folderpath);
            this.updateRequest(bean);
        }else {
            throw new RuntimeException("下载失败");
        }
    }
}
