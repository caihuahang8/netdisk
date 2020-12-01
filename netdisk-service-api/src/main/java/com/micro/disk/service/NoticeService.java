package com.micro.disk.service;

import com.micro.disk.bean.NoticeBean;
import com.micro.disk.bean.PageInfo;

import java.util.List;

public interface NoticeService {

	public PageInfo<NoticeBean> findList(Integer page, Integer limit, String userid);
	public void updateReadStatus(String userid);
	public void delete(String userid);

	public List<NoticeBean> findNotices(String userid);
	public int findNoticesCount(String userid);
}
