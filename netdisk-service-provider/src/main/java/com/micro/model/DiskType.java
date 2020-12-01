package com.micro.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;


/**
 * 文件分类（比如：文档、图片、视频、音乐、其他）
 * @author Administrator
 *
 */
@Table(
	name="disk_type",
	uniqueConstraints={
		@UniqueConstraint(columnNames={"code"})
	},
	indexes = {
		@Index(columnList = "code")
	}
)
@Entity
@Data
public class DiskType implements Serializable{
	@Id
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid")
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;

	@Column(name="code",columnDefinition="VARCHAR(50)")
	private String code;

	@Column(name="name",columnDefinition="VARCHAR(50)")
	private String name;
}
