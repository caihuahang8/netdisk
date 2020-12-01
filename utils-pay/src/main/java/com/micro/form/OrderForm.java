package com.micro.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderForm implements Serializable {
    private String userid;
    private String username;
    private String telephone;
    private Integer buymonths;// 购买的月份
}
