package com.micro.lock;

import com.sun.xml.internal.bind.v2.TODO;

public class LockContext implements Lock{
    private String locktype;
    private String host;
    private Lock lock;

    public LockContext(String locktype,String host){
        this.locktype=locktype;
        this.host=host;
    }
    @Override
    public void getLock(String lockname) {
        if("Zookeeper".equals(locktype)){
            lock=LockZookeeper.getInstance(host);
        }else if("Redis".equals(locktype)){
            //TODO
        }else{
            throw new RuntimeException("找不到标识locktype=="+locktype);
        }
        lock.getLock(lockname);
    }

    @Override
    public void unLock(String lockname) {
        lock.unLock(lockname);
    }
}
