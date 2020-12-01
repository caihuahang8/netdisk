package com.micro.disk.user.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserBean implements Serializable {
    private String nickname;
    private String username;
    private String telephone;
    private String password;
}
