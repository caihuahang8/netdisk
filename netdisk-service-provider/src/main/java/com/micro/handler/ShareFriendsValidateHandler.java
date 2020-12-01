package com.micro.handler;

import com.micro.common.ValidateUtils;
import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.ShareFriendsRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
@Component
public class ShareFriendsValidateHandler extends Handler {
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof ShareFriendsRequest){
            ShareFriendsRequest bean=(ShareFriendsRequest) request;

            if(CollectionUtils.isEmpty(bean.getIds())){
                throw new RuntimeException("请选择需要分享的记录");
            }
            if(CollectionUtils.isEmpty(bean.getFriends())){
                throw new RuntimeException("请选择好友");
            }
            ValidateUtils.validate(bean.getTitle(), "分享标题");
            ValidateUtils.validate(bean.getUserid(), "分享人ID");
            ValidateUtils.validate(bean.getUsername(), "分享人姓名");
            ValidateUtils.validate(bean.getType(), "分享来源");

            if(bean.getType()!=0&&bean.getType()!=1){
                throw new RuntimeException("分享来源格式不对");
            }

        }else{
            throw new RuntimeException("ShareFriendsValidateHandler==参数不对");
        }
    }
}
