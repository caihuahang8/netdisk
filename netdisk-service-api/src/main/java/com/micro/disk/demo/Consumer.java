package com.micro.disk.demo;

import java.util.ArrayList;

public class Consumer<T>  {
    Object object;
    ArrayList<Integer> arrayList;
    public Consumer(Object object,ArrayList arrayList){
        this.object = object;
        this.arrayList = arrayList;
    }
    public void consumer(){
        try {
            while (arrayList.isEmpty()){
                object.wait();
            }
            System.out.println("生产消息");
            arrayList.add(1);
            object.notifyAll();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
