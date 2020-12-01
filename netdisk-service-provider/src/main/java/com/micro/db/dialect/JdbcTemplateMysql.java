package com.micro.db.dialect;

import com.micro.disk.bean.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
@ConditionalOnProperty(name="db.dialect",havingValue="mysql")
public class JdbcTemplateMysql implements IJdbcTemplate {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public void update(String sql, Object[] args) {

    }

    @Override
    public Integer findCount(String sql, Object[] args) {
        return null;
    }

    @Override
    public Map<String, Object> findOne(String sql, Object[] args) {
        return null;
    }

    @Override
    public List<Map<String, Object>> findList(String sql, Object[] args) {
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public PageInfo<Map<String, Object>> findPageList(String sql, Object[] args, Integer page, Integer limit) {
        page=page==null?1:page;
        limit=limit==null?10:limit;
        //sql=sql(sql)
        int first = (page-1)*limit;
        String sqlList = "SELECT t.* FROM ("+sql+") as t limit  "+first+","+limit+" ";
        String sqlCount = "SELECT count(1) FROM ("+sql+") as t";

        List<Map<String, Object>> lists = jdbcTemplate.queryForList(sqlList,args);
        Integer count = jdbcTemplate.queryForObject(sqlCount,Integer.class,args);
        int pageCount = (count%limit==0)?(count/limit):(count/limit+1);
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>();

        pageInfo.setRows(lists);
        pageInfo.setTotalPage(pageCount);
        pageInfo.setTotalElements(count);
        return pageInfo;
    }
}
