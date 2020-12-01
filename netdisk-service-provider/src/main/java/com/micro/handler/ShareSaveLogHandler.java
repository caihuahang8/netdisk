package com.micro.handler;


import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskShareDao;
import com.micro.db.dao.DiskShareSaveDao;
import com.micro.model.DiskShareSave;
import com.micro.param.ShareSaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ShareSaveLogHandler extends Handler {
	@Autowired
	private DiskShareDao diskShareDao;
	@Autowired
	private DiskShareSaveDao diskShareSaveDao;

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareSaveRequest){
			ShareSaveRequest bean=(ShareSaveRequest) request;

			//分享-保存数量（考虑并发，应该加分布式锁）
			diskShareDao.updateCount(bean.getShareid());

			//分享-保存明细
			DiskShareSave dss=new DiskShareSave();
			dss.setShareid(bean.getShareid());
			dss.setUserid(bean.getUserid());
			dss.setUsername(bean.getUsername());
			dss.setCreatetime(new Date());
			diskShareSaveDao.save(dss);
		}else{
			throw new RuntimeException("ShareSaveLogHandler==参数不对");
		}
	}
}
