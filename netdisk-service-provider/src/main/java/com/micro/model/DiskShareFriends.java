package com.micro.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 分享-接收人
 * @author Administrator
 *
 */
@Table(name="disk_share_friends",
indexes = {
	@Index(columnList = "shareid"),
	@Index(columnList = "userid"),
}
)
@Entity
@Data
public class DiskShareFriends {
	@Id
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid")
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;

	@Column(name="shareid",columnDefinition="VARCHAR(50)")
	private String shareid;

	@Column(name="userid",columnDefinition="VARCHAR(50)")
	private String userid;

	@Column(name="username",columnDefinition="VARCHAR(50)")
	private String username;
}
