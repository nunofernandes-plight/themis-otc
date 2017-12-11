package com.oxchains.themis.notice.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author luoxuri
 * @create 2017-10-24 18:39
 **/
@Entity
@Data
@Table(name = "btc_market")
public class BTCMarket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "cname")
    private String cName;

    @Column(name = "coinid")
    private Long coinId;

    @Column(name = "coinname")
    private String coinName;

    @Column(name = "coinsign")
    private String coinSign;

    @Column(name = "exebyrate")
    private Long exeByRate;

    @Column(name = "isrecomm")
    private Long isRecomm;

    @Column(name = "markvalue")
    private String marketValue;

    @Column(name = "moneytype")
    private Long moneyType;

    private String name;
    private String symbol;
    private String time;
    private Long type;

    @Transient
    private BTCTicker ticker;

}


