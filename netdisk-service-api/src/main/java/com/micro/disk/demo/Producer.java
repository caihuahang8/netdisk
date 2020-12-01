package com.micro.disk.demo;

import java.util.ArrayList;

public class Producer<T>{
    Object object;
    ArrayList<T> arrayList;
    public Producer(Object object,ArrayList arrayList){
        this.object = object;
        this.arrayList = arrayList;
    }
    public void producer(){
        try{
            System.out.println(11);
            while (arrayList.isEmpty()){
                object.wait();
            }
            System.out.println("消费消息");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
