package com.micro.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Table(
	name="disk_type_suffix_operate",
	uniqueConstraints={
		@UniqueConstraint(columnNames={"suffix","componentcode"})
	},
	indexes = {
		@Index(columnList = "suffix")
	}
)
@Entity
@Data
public class DiskTypeSuffixOperate implements Serializable{
	@Id
	@GenericGenerator(name = "uuid",strategy = "uuid")
	@GeneratedValue(generator = "uuid")
	@Column(name="id",columnDefinition="VARCHAR(50)")
	private String id;

	@Column(name="suffix",columnDefinition="VARCHAR(50)")
	private String suffix;

	@Column(name="componentcode",columnDefinition="VARCHAR(50)")
	private String componentcode;//组件名称
}
