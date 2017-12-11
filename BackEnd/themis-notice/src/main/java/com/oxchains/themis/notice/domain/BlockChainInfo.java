package com.oxchains.themis.notice.domain;

import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.Objects;

/**
 * @author luoxuri
 * @create 2017-11-02 11:00
 **/
@Entity
@Data
public class BlockChainInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Transient
    private CNYDetail CNY;

    private String symbol;

    @Column(name = "savetime")
    private String saveTime;

}
