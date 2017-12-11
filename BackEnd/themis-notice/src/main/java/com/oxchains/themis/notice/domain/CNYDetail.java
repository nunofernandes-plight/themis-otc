package com.oxchains.themis.notice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * blockchaininfo 上 比特币->人民币的价格
 * @author luoxuri
 * @create 2017-11-02 11:45
 **/
@Entity
@Data
@Table(name = "bci_cny")
public class CNYDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String last;
    private String buy;
    private String sell;
    private String symbol;

    @Column(name = "savetime")
    private String saveTime;
}
