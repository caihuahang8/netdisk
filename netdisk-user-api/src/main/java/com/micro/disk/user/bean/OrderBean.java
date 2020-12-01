package com.micro.disk.user.bean;

import lombok.Data;

import java.io.Serializable;


@Data
public class OrderBean implements Serializable {
    private String orderid;
    private String userid;
    private String username;
    private String telephone;
    private Integer buymonths;// 购买的月份
    private String orderAmount;
}
