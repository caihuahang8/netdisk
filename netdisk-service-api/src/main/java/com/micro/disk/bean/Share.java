package com.micro.disk.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Share implements Serializable {
    private String id;
    private String title;
    private String shareuser;
    private String sharetime;

    private Integer type;//0私密链接分享，1好友分享
    private String url;//【属于私密分享】链接地址
    private String code;//【属于私密分享】提取码

    private String effectname;
    private Integer sharetype;//0有提取码，1无提取码
    private Integer status;//0正常，1已失效，2已撤销
    private Integer savecount;//保存次数（具体明细跟mongodb日志库关联）
}
