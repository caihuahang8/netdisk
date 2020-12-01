package com.micro.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(
	name="disk_type_component",
	uniqueConstraints={
		@UniqueConstraint(columnNames={"code"})
	},
	indexes = {
		@Index(columnList = "code")
	}
)
@Entity
@Data
public class DiskTypeComponent implements Serializable{
	@Id
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid")
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;

	@Column(name="code",columnDefinition="VARCHAR(50)")
	private String code;

	@Column(name="name",columnDefinition="VARCHAR(50)")
	private String name;

	private String remark;

	@Column(name="createuserid",columnDefinition="VARCHAR(50)")
	private String createuserid;

	@Column(name="createusername",columnDefinition="VARCHAR(50)")
	private String createusername;

	private Date createtime;
}
