package com.micro.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.common.CapacityUtils;
import com.micro.db.dao.DiskUserCapacityDao;
import com.micro.db.dao.DiskUserCapacityHistoryDao;
import com.micro.disk.bean.UserCapacityBean;
import com.micro.lock.LockContext;
import com.micro.model.DiskUserCapacity;
import com.micro.model.DiskUserCapacityHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service(interfaceClass= com.micro.disk.service.UserCapacityService.class)
@Component
@Transactional
public class UserCapacityService implements com.micro.disk.service.UserCapacityService {
    @Autowired
    private DiskUserCapacityDao diskUserCapacityDao;
    @NacosValue(value="${locktype}",autoRefreshed=true)
    private String locktype;
    @NacosValue(value="${lockhost}",autoRefreshed=true)
    private String lockhost;
    @Autowired
    private DiskUserCapacityHistoryDao diskUserCapacityHistoryDao;
    @Override
    public UserCapacityBean findUserCapacity(String userid) {
        DiskUserCapacity diskUserCapacity =  diskUserCapacityDao.findByUserid(userid);
        UserCapacityBean ucb=new UserCapacityBean();
        if(ucb==null){
            ucb.setUserid(userid);
            ucb.setTotalcapacity(0L);
            ucb.setUsedcapacity(0L);
            ucb.setTotalcapacityname("0B");
            ucb.setTotalcapacityname("0B");
        }else {
            ucb.setUserid(userid);
            ucb.setTotalcapacity(diskUserCapacity.getTotalcapacity());
            ucb.setTotalcapacityname(CapacityUtils.convert(diskUserCapacity.getTotalcapacity()));
            ucb.setUsedcapacity(diskUserCapacity.getUsedcapacity());
            ucb.setUsedcapacityname(CapacityUtils.convert(diskUserCapacity.getUsedcapacity()));
        }
        return ucb;
    }

    @Override
    public void updateUserCapacity(int type, long capacity, String userid, String username, String remark) {
        LockContext lockContext = new LockContext(locktype,lockhost);
        try{
            lockContext.getLock("capacity-"+userid);
            //更新容量
            if (type==0){
                //新增已用容量，减少总容量
                diskUserCapacityDao.addUsedCapacity(capacity,userid);
            }else if(type==1){
                diskUserCapacityDao.reduceUsedCapacity(capacity,userid);
            }else {
                throw new RuntimeException("type格式不对");
            }

            //计算剩余容量
            DiskUserCapacity bean = diskUserCapacityDao.findByUserid(userid);
            if (bean==null){
                throw new RuntimeException("查询不到您的容量记录，无法操作！ ");
            }
            long lefttotal = bean.getTotalcapacity()-bean.getUsedcapacity();


            //历史记录
            DiskUserCapacityHistory history=new DiskUserCapacityHistory();
            history.setUserid(userid);
            history.setRemark(remark);

            history.setCapacity(capacity);//新增减少的容量
            history.setType(type);//0减少，1新增
            history.setLeftcapacity(lefttotal);//总剩余容量

            history.setCreateuserid(userid);
            history.setCreateusername(username);
            history.setCreatetime(new Date());
            diskUserCapacityHistoryDao.save(history);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            lockContext.unLock("capacity-"+userid);
        }
    }

    @Override
    public void init(String userid, String username) {
        DiskUserCapacity bean=diskUserCapacityDao.findByUserid(userid);
        if(bean==null){//不存在则初始化
            bean=new DiskUserCapacity();

            //容量保存
            bean.setUserid(userid);
            bean.setTotalcapacity(100*1024*1024*1024L);//100G
            bean.setUsedcapacity(0L);
            diskUserCapacityDao.save(bean);

            //历史记录保存
            DiskUserCapacityHistory history=new DiskUserCapacityHistory();
            history.setUserid(userid);
            history.setRemark("分配容量");

            history.setCapacity(bean.getTotalcapacity());//新增容量
            history.setType(1);//0减少，1新增
            history.setLeftcapacity(bean.getTotalcapacity());//剩余容量

            history.setCreateuserid(userid);
            history.setCreateusername(username);
            history.setCreatetime(new Date());
            diskUserCapacityHistoryDao.save(history);
        }
    }

    /**
     * 增加容量（VIP）
     * @param type
     * @param capacity
     * @param userid
     * @param username
     * @param remark
     */
    @Override
    public void addCapacity(int type, long capacity, String userid, String username, String remark) {
        //1.并发锁
        LockContext lockContext = new LockContext(locktype,lockhost);
        try{
            lockContext.getLock("capacity-"+userid);
            //2.增加容量
            diskUserCapacityDao.addUsedCapacity(capacity,userid);
            //计算剩余容量
            DiskUserCapacity bean = diskUserCapacityDao.findByUserid(userid);
            if (bean==null){
                throw new RuntimeException("查询不到您的容量记录，无法操作！ ");
            }
            long lefttotal = bean.getTotalcapacity()-bean.getUsedcapacity();

            //3.记录历史
            DiskUserCapacityHistory history=new DiskUserCapacityHistory();
            history.setUserid(userid);
            history.setRemark(remark);

            history.setCapacity(capacity);//新增减少的容量
            history.setType(type);//0减少，1新增
            history.setLeftcapacity(lefttotal);//总剩余容量

            history.setCreateuserid(userid);
            history.setCreateusername(username);
            history.setCreatetime(new Date());
            diskUserCapacityHistoryDao.save(history);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            lockContext.unLock("capacity-"+userid);
        }
    }

    /**
     * 减少容量（VIP）
     * @param type
     * @param capacity
     * @param userid
     * @param username
     * @param remark
     */
    @Override
    public void reduceCapacity(int type, long capacity, String userid, String username, String remark){
        //1.并发锁
        LockContext lockContext = new LockContext(locktype,lockhost);
        try{
            lockContext.getLock("capacity-"+userid);
            //2.减少容量
            diskUserCapacityDao.reduceUsedCapacity(capacity,userid);

            DiskUserCapacity bean = diskUserCapacityDao.findByUserid(userid);
            if (bean==null){
                throw new RuntimeException("查询不到您的容量记录，无法操作！ ");
            }
            long lefttotal = bean.getTotalcapacity()-bean.getUsedcapacity();

            //3.记录历史
            DiskUserCapacityHistory history=new DiskUserCapacityHistory();
            history.setUserid(userid);
            history.setRemark(remark);

            history.setCapacity(capacity);//新增减少的容量
            history.setType(0);//0减少，1新增
            history.setLeftcapacity(lefttotal);//总剩余容量

            history.setCreateuserid(userid);
            history.setCreateusername(username);
            history.setCreatetime(new Date());
            diskUserCapacityHistoryDao.save(history);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            lockContext.unLock("capacity-"+userid);
        }

    }


}
