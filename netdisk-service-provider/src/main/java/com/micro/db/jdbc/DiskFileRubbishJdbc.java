package com.micro.db.jdbc;

import com.micro.common.CapacityUtils;
import com.micro.db.dialect.IJdbcTemplate;
import com.micro.disk.bean.FileListBean;
import com.micro.disk.bean.PageInfo;
import com.micro.disk.bean.RubbishBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DiskFileRubbishJdbc {

    @Autowired
    private IJdbcTemplate jdbcTemplate;

    public PageInfo<RubbishBean> findRubbishList(Integer page,Integer limit,String userid,String orderfield,String ordertype){
        PageInfo<RubbishBean> pageInfo=new PageInfo<RubbishBean>();
        try{
            String sql="select id,filename,filesize,date_format(deletetime,'%Y-%m-%d %H:%i:%S') as deletetime, "
                    + " (select icon from disk_type_suffix where suffix=df.filesuffix and typecode=df.typecode) as fileicon "
                    + "from disk_file_del df where createuserid=?";



            if(!StringUtils.isEmpty(orderfield)){
                sql+=" order by deletetime desc";
            }else{
                ordertype=ordertype.replace("ending", "");
                sql+=" order by "+orderfield+" "+ordertype;
            }
            Object[] args={
                    userid
            };

            PageInfo<Map<String, Object>> fileListBeanPageInfo = jdbcTemplate.findPageList(sql,args,page,limit);
            List<RubbishBean> rows = new ArrayList<>();
            if(!CollectionUtils.isEmpty(fileListBeanPageInfo.getRows())){
                for (Map<String,Object> map: fileListBeanPageInfo.getRows() ){
                    RubbishBean fileListBean = new RubbishBean();
                    fileListBean.setId(map.get("id")==null?"":map.get("id").toString());
                    fileListBean.setFilename(map.get("filename")==null?"":map.get("filename").toString());
                    fileListBean.setFilesize(CapacityUtils.convert(Long.parseLong(map.get("filesize")==null?"0":map.get("filesize").toString())));
                    fileListBean.setDeletetime(map.get("deletetime")==null?"":map.get("deletetime").toString());
                    fileListBean.setFileicon(map.get("fileicon")==null?"":map.get("fileicon").toString());
                    rows.add(fileListBean);
                }
            }

            pageInfo.setPage(page);
            pageInfo.setLimit(limit);
            pageInfo.setRows(rows);
            pageInfo.setTotalElements(fileListBeanPageInfo.getTotalElements());
            pageInfo.setTotalPage(fileListBeanPageInfo.getTotalPage());
            pageInfo.setCode(0);
            pageInfo.setMsg("查询成功");
            return pageInfo;
        }catch (Exception e){
            pageInfo.setCode(1);
            pageInfo.setMsg("查询失败");
            return pageInfo;
        }
    }
}
