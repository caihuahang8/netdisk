package com.micro.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Table(
        name="disk_album_file",
        uniqueConstraints={@UniqueConstraint(columnNames={"fileid"})},   //唯一约束
        indexes = {
                @Index(columnList = "albumid"),
                @Index(columnList = "fileid")
        }
)
@Entity
@Data
public class DiskAlbumFile implements Serializable {
    @Id
    @GenericGenerator(name = "uuid",strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name="id",columnDefinition="VARCHAR(50)")
    private String id;

    @Column(name="albumid",columnDefinition="VARCHAR(50)")
    private String albumid;

    @Column(name="fileid",columnDefinition="VARCHAR(255)")
    private String fileid;

    private Date createtime;
}
