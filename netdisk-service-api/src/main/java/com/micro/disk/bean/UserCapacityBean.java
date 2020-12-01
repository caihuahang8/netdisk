package com.micro.disk.bean;

import lombok.Data;

@Data
public class UserCapacityBean {
    private String id;
    private String userid;

    private Long totalcapacity;
    private String totalcapacityname;

    private Long usedcapacity;
    private String usedcapacityname;
}
