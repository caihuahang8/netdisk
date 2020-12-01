package com.micro.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Table(
	name="disk_notice",
	indexes = {
		@Index(columnList = "type"),
		@Index(columnList = "userid")
	}
)
@Entity
@Data
public class DiskNotice {
	@Id
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid")
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;

	@Column(name="type",columnDefinition="VARCHAR(50)")
	private String type;//通知类型

	@Column(name="typename",columnDefinition="VARCHAR(50)")
	private String typename;//通知类型

	@Column(name="content",columnDefinition="VARCHAR(500)")
	private String content;//通知内容

	@Column(name="userid",columnDefinition="VARCHAR(50)")
	private String userid;

	@Column(name="username",columnDefinition="VARCHAR(50)")
	private String username;

	private Date createtime;

	private Integer status;//0未阅读，1已阅读
	private Date readtime;//阅读时间

}
