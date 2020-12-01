package com.micro.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Table(name="disk_share_save",
indexes = {
	@Index(columnList = "shareid"),
	@Index(columnList = "userid"),
}
)
@Entity
@Data
public class DiskShareSave {
	@Id
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid")
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;

	@Column(name="shareid",columnDefinition="VARCHAR(50)")
	private String shareid;

	private String userid;
	private String username;
	private Date createtime;
}
