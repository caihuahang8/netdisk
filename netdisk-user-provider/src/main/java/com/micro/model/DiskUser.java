package com.micro.model;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;
@Table(
        name="disk_user",
        uniqueConstraints={@UniqueConstraint(columnNames={"telephone"})},   //唯一约束
        indexes = {
                @Index(columnList = "username"),
                @Index(columnList = "password")
        }
)
@Entity
@Data
public class DiskUser {
    @Id
    @GenericGenerator(name = "uuid",strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name="id",columnDefinition="VARCHAR(50)")
    private String id;
    @Column(name="nickname",columnDefinition="VARCHAR(50)")
    private String nickname;
    @Column(name="username",columnDefinition="VARCHAR(50)")
    private String username;
    @Column(name="password",columnDefinition="VARCHAR(50)")
    private String password;
    @Column(name="telephone",columnDefinition="VARCHAR(50)")
    private String telephone;
    @Column(name="token",columnDefinition="VARCHAR(20)")
    private String token;//登陆身份    1.会员 2.普通会员

    private Date createtime;
}
