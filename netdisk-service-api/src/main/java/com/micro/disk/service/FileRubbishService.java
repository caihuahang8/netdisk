package com.micro.disk.service;

import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.RubbishBean;

import java.util.List;

public interface FileRubbishService {
    PageInfo<RubbishBean> findRubbish(String userid, Integer page, Integer limit);
    void deleteRubbish(List<String> ids,String userid);
    //folderid ids username userid
    void recover(String folderid,List<String> ids,String username,String userid);
}
