package com.micro.websocket;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable{
	private Integer type;//0容量，1通知
	private Object data;
	private Integer count;
}
