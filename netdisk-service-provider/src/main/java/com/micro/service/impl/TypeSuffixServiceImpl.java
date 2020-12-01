package com.micro.service.impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.db.dao.DiskTypeDao;
import com.micro.db.dao.DiskTypeSuffixDao;

import com.micro.db.jdbc.DiskTypeSuffixOperateJdbc;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.TypeSuffixBean;
import com.micro.disk.bean.TypeSuffixOperateBean;
import com.micro.disk.service.TypeSuffixService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;

@Service(interfaceClass=TypeSuffixService.class)
@Transactional
@Component
public class TypeSuffixServiceImpl implements TypeSuffixService{
	@Autowired
	private DiskTypeSuffixDao diskTypeSuffixDao;
	@Autowired
	private DiskTypeDao diskTypeDao;
	@Autowired
	private DiskTypeSuffixOperateJdbc diskTypeSuffixOperateJdbc;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	private JsonUtils jsonUtils=new JsonJackUtils();


	@Override
	public void save(String typecode, String name, String icon, String iconbig, String suffix, List<String> operatecodes) {

	}

	@Override
	public void update(String id, String name, String icon, String iconbig, String suffix, List<String> operatecodes) {

	}

	@Override
	public void delete(String id) {

	}

	@Override
	public TypeSuffixBean findOne(String id) {
		return null;
	}

	@Override
	public PageInfo<TypeSuffixBean> findList(Integer page, Integer limit, String typecode) {
		return null;
	}

	@Override
	public List<TypeSuffixBean> findList(String typecode) {
		return null;
	}

	@Override
	public TypeSuffixBean findBySuffix(String suffix) {
		return null;
	}

	@Override
	public List<TypeSuffixOperateBean> findComponentsBySuffix(String suffix) {
		return diskTypeSuffixOperateJdbc.findListBySuffix(suffix);
	}

	@Override
	public boolean isSupportSuffix(String suffix) {
		return false;
	}


}
