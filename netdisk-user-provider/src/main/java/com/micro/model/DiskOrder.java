package com.micro.model;

import com.micro.enums.OrderStatusEnum;
import com.micro.enums.PayStatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(
        name="disk_order",
        uniqueConstraints={@UniqueConstraint(columnNames={"orderid"})},
        indexes = {
                @Index(columnList = "orderid")
        }//唯一约束
)
@Entity
@Data
public class DiskOrder {
        @Id
        @Column(name="orderid",columnDefinition="VARCHAR(50)")
        private String orderid;
        @Column(name="username",columnDefinition="VARCHAR(50)")
        private String username;
        @Column(name="telephone",columnDefinition="VARCHAR(50)")
        private String telephone;//  电话号码
        @Column(name="token",columnDefinition="VARCHAR(20)")
        private String token;//是否会员    1.超级会员 2.普通会员
        /** 订单总金额. */
        @Column(name="orderAmount",columnDefinition = "VARCHAR(20)")
        private String orderAmount;

        /** 订单状态, 默认为0新下单. */
        @Column(name="orderStatus",columnDefinition = "int(5)")
        private Integer orderStatus = OrderStatusEnum.NEW.getCode();

        /** 支付状态, 默认为0未支付. */
        @Column(name="payStatus",columnDefinition = "int(5)")
        private Integer payStatus = PayStatusEnum.WAIT.getCode();
        private Date createtime;
        private Date updateTime;
}
