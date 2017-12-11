package com.oxchains.themis.repo.entity;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by huohuo on 2017/10/25.
 * @author huohuo
 */
@Entity
@Table(name = "order_arbitrate")
@Data
public class OrderArbitrate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    private Long userId;
    private  String buyerAuth;
    private String sellerAuth;
    private Integer status;
    private String userAuth;

    public OrderArbitrate() {
    }

    public OrderArbitrate(String orderId, Long userId, Integer status, String userAuth) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.userAuth = userAuth;
    }
}
