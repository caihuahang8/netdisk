package com.micro.db.jdbc;

import com.micro.common.CapacityUtils;
import com.micro.db.dialect.IJdbcTemplate;
import com.micro.disk.bean.FileListBean;
import com.micro.disk.bean.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DiskFileJdbc {
    @Autowired
    private IJdbcTemplate jdbcTemplate;

    public PageInfo<FileListBean> findAllList(Integer page,Integer limit,String userid,String pid,String typecode,String orderfield,String ordertype){
        PageInfo<FileListBean> pageInfo=new PageInfo<FileListBean>();
        try {
            String sql="select id,filename,filesize,filetype,filemd5,filesuffix,date_format(createtime,'%Y-%m-%d %H:%i:%S') as createtime, "
                    + " (select icon from disk_type_suffix where suffix=df.filesuffix and typecode=df.typecode) as fileicon "
                    + "from disk_file df where createuserid=?";
            //pid
            if (!StringUtils.isEmpty(pid)){
                sql+="and pid= '"+pid+"'";
            }
            //typecode
            if(!StringUtils.isEmpty(typecode)&!"all".equals(typecode)&&!"ALL".equals(typecode)){
                sql+=" and typecode='"+typecode+"'";
            }
            if(!StringUtils.isEmpty(orderfield)){
                sql+=" order by createtime desc";
            }else{
                ordertype=ordertype.replace("ending", "");
                sql+=" order by "+orderfield+" "+ordertype;
            }

            Object[] args={
                    userid
            };

            PageInfo<Map<String, Object>> fileListBeanPageInfo = jdbcTemplate.findPageList(sql,args,page,limit);
            List<FileListBean> rows = new ArrayList<>();

            if(!CollectionUtils.isEmpty(fileListBeanPageInfo.getRows())){
                for (Map<String,Object> map: fileListBeanPageInfo.getRows() ){
                    FileListBean fileListBean = new FileListBean();
                    fileListBean.setId(map.get("id")==null?"":map.get("id").toString());
                    fileListBean.setFilename(map.get("filename")==null?"":map.get("filename").toString());
                    fileListBean.setFilesuffix(map.get("filesuffix")==null?"":map.get("filesuffix").toString());
                    fileListBean.setFilemd5(map.get("filemd5")==null?"":map.get("filemd5").toString());
                    fileListBean.setFiletype(Integer.parseInt(map.get("filetype")==null?"0":map.get("filetype").toString()));
                    if(fileListBean.getFiletype()==0){
                        fileListBean.setFileicon(" ");
                    }else{
                        fileListBean.setFileicon(map.get("fileicon")==null?"":map.get("fileicon").toString());
                    }
                    String size= CapacityUtils.convert(map.get("filesize")==null?"":map.get("filesize").toString());
                    fileListBean.setFilesizename(size);
                    fileListBean.setFilesize(Long.parseLong(map.get("filesize")==null?"0":map.get("filesize").toString()));

                    fileListBean.setCreatetime(map.get("createtime")==null?"":map.get("createtime").toString());
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
