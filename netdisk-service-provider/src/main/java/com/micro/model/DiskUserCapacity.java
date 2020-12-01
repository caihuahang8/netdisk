package com.micro.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Table(
        name="disk_user_capacity",
        uniqueConstraints={
                @UniqueConstraint(columnNames={"userid"})
        },
        indexes = {
                @Index(columnList = "userid")
        }
)
@Entity
@Data
public class DiskUserCapacity {
    @Id
    @GenericGenerator(name = "uuid",strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name="id",columnDefinition="VARCHAR(50)")
    private String id;

    @Column(name="userid",columnDefinition="VARCHAR(50)")
    private String userid;

    private Long totalcapacity;//总容量
    private Long usedcapacity;//已用容量
}
