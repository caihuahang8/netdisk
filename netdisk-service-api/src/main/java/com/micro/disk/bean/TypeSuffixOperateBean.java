package com.micro.disk.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class TypeSuffixOperateBean implements Serializable{
	private String componentcode;//组件类型
	private String componentname;//组件名称
}
