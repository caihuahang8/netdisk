package com.micro.disk.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShareBean  implements Serializable {
    private String url;//返回的私密链接
    private String code;
}
