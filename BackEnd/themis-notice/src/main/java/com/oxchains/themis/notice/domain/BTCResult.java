package com.oxchains.themis.notice.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author luoxuri
 * @create 2017-10-24 19:01
 **/
@Entity
@Data
@Table(name = "btc_result")
public class BTCResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String des;

    @Column(name = "issuc")
    private String isSuc;

    @Transient
    private BTCMarket datas;


}
