package com.micro.db.jdbc;

import com.micro.common.CapacityUtils;
import com.micro.db.dialect.IJdbcTemplate;
import com.micro.disk.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DiskShareJdbc {
    @Autowired
    private IJdbcTemplate jdbcTemplate;

    public PageInfo<Share> findMyShare(Integer page, Integer limit, String userid, Integer type, Integer status){
        PageInfo<Share> pageInfo=new PageInfo<>();
        try{
            String sql="select * from disk_share where shareuserid=? ";
            if(type!=null){
                sql+=" and type="+type;
            }
            if(status!=null){
                sql+=" and status="+status;
            }
            sql+=" order by sharetime desc";

            Object[] args={
                    userid
            };
            PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
            List<Share> rows=new ArrayList<>();
            if(!CollectionUtils.isEmpty(pi.getRows())){
                for(Map<String,Object> map:pi.getRows()){
                    Share row=new Share();
                    row.setId(map.get("id")==null?"":map.get("id").toString());
                    row.setTitle(map.get("title")==null?"":map.get("title").toString());
                    row.setShareuser(map.get("shareusername")==null?"":map.get("shareusername").toString());
                    row.setSharetime(map.get("sharetime")==null?"":map.get("sharetime").toString());

                    row.setType(Integer.parseInt(map.get("type")==null?"0":map.get("type").toString()));
                    row.setUrl(map.get("url")==null?"":map.get("url").toString());
                    row.setCode(map.get("code")==null?"":map.get("code").toString());

                    String effectname=map.get("effect")==null?"0":map.get("effect").toString();
                    if("0".equals(effectname)){
                        effectname="永久有效";
                    }else{
                        effectname=effectname+"天";
                    }
                    row.setEffectname(effectname);
                    row.setSharetype(Integer.parseInt(map.get("sharetype")==null?"0":map.get("sharetype").toString()));
                    row.setStatus(Integer.parseInt(map.get("status")==null?"0":map.get("status").toString()));
                    row.setSavecount(Integer.parseInt(map.get("savecount")==null?"0":map.get("savecount").toString()));
                    rows.add(row);
                }
            }

            pageInfo.setPage(page);
            pageInfo.setLimit(limit);
            pageInfo.setRows(rows);
            pageInfo.setTotalElements(pi.getTotalElements());
            pageInfo.setTotalPage(pi.getTotalPage());

            pageInfo.setCode(0);
            pageInfo.setMsg("查询成功");
        }catch(Exception e){
            pageInfo.setCode(1);
            pageInfo.setMsg("查询失败");
        }
        return pageInfo;
    }
    public List<ShareFileBean> findListByPid(String shareid,String pid){
        try{
            String sql="select *,"
                    + "date_format(dsf.createtime,'%Y-%m-%d %H:%i:%S') as createtimes, "
                    + " (select icon from disk_type_suffix where suffix=dsf.filesuffix and typecode=dsf.typecode) as fileicon "
                    + "from disk_share_file dsf "
                    + "where shareid=? and pid=? order by createtime desc";
            Object[] args={
                    shareid,
                    pid
            };
            List<Map<String,Object>> lists=jdbcTemplate.findList(sql, args);
            List<ShareFileBean> files=new ArrayList<>();
            if(!CollectionUtils.isEmpty(lists)){
                for(Map map:lists){
                    ShareFileBean bean=new ShareFileBean();
                    bean.setId(map.get("id")==null?"":map.get("id").toString());
                    bean.setFilename(map.get("filename")==null?"":map.get("filename").toString());
                    bean.setFilesize(CapacityUtils.convert(map.get("filesize")==null?"":map.get("filesize").toString()));
                    bean.setFiletype(Integer.parseInt(map.get("filetype")==null?"0":map.get("filetype").toString()));
                    bean.setCreatetime(map.get("createtimes")==null?"":map.get("createtimes").toString());
                    if(bean.getFiletype()==0){
                        bean.setFileicon("");
                    }else{
                        bean.setFileicon(map.get("fileicon")==null?"":map.get("fileicon").toString());
                    }
                    files.add(bean);
                }
            }
            return files;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("查询失败");
        }
    }
    public PageInfo<Share> findFriendsShare(Integer page,Integer limit,String userid,Integer status){
        PageInfo<Share> pageInfo=new PageInfo<>();
        try{
            String sql="select ds.id,ds.title,ds.shareusername,ds.sharetime,ds.status, "
                    + " (select count(1) from disk_share_save where shareid=ds.id and userid='"+userid+"') zcount "
                    + " from disk_share ds,disk_share_friends dsf "
                    + " where ds.id=dsf.shareid and dsf.userid=? ";
            if(status!=null){
                sql+=" and ds.status="+status;
            }
            sql+=" order by ds.sharetime desc";

            Object[] args={
                    userid
            };
            PageInfo<Map<String,Object>> pi=jdbcTemplate.findPageList(sql, args, page, limit);
            List<Share> rows=new ArrayList<>();
            if(!CollectionUtils.isEmpty(pi.getRows())){
                for(Map<String,Object> map:pi.getRows()){
                    Share row=new Share();
                    row.setId(map.get("id")==null?"":map.get("id").toString());
                    row.setTitle(map.get("title")==null?"":map.get("title").toString());
                    row.setShareuser(map.get("shareusername")==null?"":map.get("shareusername").toString());
                    row.setSharetime(map.get("sharetime")==null?"":map.get("sharetime").toString());
                    row.setStatus(Integer.parseInt(map.get("status")==null?"0":map.get("status").toString()));
                    row.setSavecount(Integer.parseInt(map.get("zcount")==null?"0":map.get("zcount").toString()));
                    rows.add(row);
                }
            }
            pageInfo.setPage(page);
            pageInfo.setLimit(limit);
            pageInfo.setRows(rows);
            pageInfo.setTotalElements(pi.getTotalElements());
            pageInfo.setTotalPage(pi.getTotalPage());

            pageInfo.setCode(0);
            pageInfo.setMsg("查询成功");
        }catch (Exception e){
            pageInfo.setCode(1);
            pageInfo.setMsg("查询失败");
        }
        return pageInfo;
    }
    public List<ShareFriendsBean> findFriends(String shareid){
        String sql="select dsf.userid,dsf.username,"
                + " (select createtime from disk_share_save where shareid=dsf.shareid and userid=dsf.userid limit 0,1) as zctime "
                + " from disk_share_friends dsf where shareid=?";
        Object[] args={
                shareid
        };
        List<Map<String,Object>> lists=jdbcTemplate.findList(sql, args);
        List<ShareFriendsBean> rows=new ArrayList<>();
        if(!CollectionUtils.isEmpty(lists)){
            for(Map<String,Object> map:lists){
                ShareFriendsBean row=new ShareFriendsBean();
                row.setUserid(map.get("userid")==null?"":map.get("userid").toString());
                row.setUsername(map.get("username")==null?"":map.get("username").toString());

                String zctime=map.get("zctime")==null?"":map.get("zctime").toString();
                String zcstatus="";
                if("".equals(zctime)){
                    zcstatus="未转存";
                }else{
                    zcstatus="已转存";
                }

                row.setZcstatus(zcstatus);
                row.setZctime(zctime);

                rows.add(row);
            }
        }
        return rows;
    }

}
