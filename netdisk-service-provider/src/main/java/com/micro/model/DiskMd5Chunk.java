package com.micro.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 切块存储
 * @author Administrator
 *
 */
@Table(
        name="disk_md5_chunk",
        indexes = {
                @Index(columnList = "filemd5"),
                @Index(columnList = "chunknumber"),
                @Index(columnList = "storepath")
        },
        uniqueConstraints={
                @UniqueConstraint(columnNames={"filemd5","chunknumber"})
        }
)
@Entity
@Data
public class DiskMd5Chunk implements Serializable {
    @Id
    @GenericGenerator(name = "uuid",strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name="id",columnDefinition="VARCHAR(50)")
    private String id;

    //@Column(name="chunkmd5",columnDefinition="VARCHAR(200)")
    //private String chunkmd5;//切块的md5

    @Column(name="filemd5",columnDefinition="VARCHAR(200)")
    private String filemd5;//文件的md5

    @Column(name="chunkname",columnDefinition="VARCHAR(200)")
    private String chunkname;

    private Integer chunknumber;//第几块

    private long chunksize;//切块大小

    private Integer totalchunks;//总切块数

    private long totalsize;//总大小

    @Column(name="storepath",columnDefinition="VARCHAR(255)")
    private String storepath;//存储目录
}
