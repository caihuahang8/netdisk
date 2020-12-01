package com.micro.lock;

public interface Lock {
    void getLock(String lockname);
    //释放锁
    void unLock(String lockname);
}
