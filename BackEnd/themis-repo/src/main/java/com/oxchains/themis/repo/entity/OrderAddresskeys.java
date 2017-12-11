package com.oxchains.themis.repo.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by huohuo on 2017/10/25.
 * @author huohuo
 */
@Entity
@Table(name = "order_address_key")
@Data
public class OrderAddresskeys implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        //公私匙表唯一id
    private String orderId;  //相关联的订单id

    private String buyerPubAuth; //买家公匙

    private String buyerPriAuth;//买家私匙
    private String buyerSellerPriAuth; //买家拥有的买家的私匙

    private String sellerPubAuth; //卖家公匙

    private String sellerPriAuth; //卖家私匙
    private String sellerBuyerPriAuth; //卖家拥有的买家的私匙
    private String userPubAuth; //仲裁者公匙
    private String userPriAuth; //仲裁者私匙

    @Override
    public String toString() {
        return "OrderAddresskeys{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", buyerPubAuth='" + buyerPubAuth + '\'' +
                ", buyerPriAuth='" + buyerPriAuth + '\'' +
                ", buyerSellerPriAuth='" + buyerSellerPriAuth + '\'' +
                ", sellerPubAuth='" + sellerPubAuth + '\'' +
                ", sellerPriAuth='" + sellerPriAuth + '\'' +
                ", sellerBuyerPriAuth='" + sellerBuyerPriAuth + '\'' +
                ", userPubAuth='" + userPubAuth + '\'' +
                ", userPriAuth='" + userPriAuth + '\'' +
                '}';
    }
    public OrderAddresskeys(String orderId, String buyerPubAuth, String buyerPriAuth, String userPubAuth, String userPriAuth) {
        this.orderId = orderId;
        this.buyerPubAuth = buyerPubAuth;
        this.buyerPriAuth = buyerPriAuth;
        this.userPubAuth = userPubAuth;
        this.userPriAuth = userPriAuth;
    }
    public OrderAddresskeys() {
    }
}
