package com.oxchains.themis.notice.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author luoxuri
 * @create 2017-10-24 18:45
 **/
@Entity
@Data
@Table(name = "btc_ticker")
public class BTCTicker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal buy;
    private BigDecimal buydollar;
    private BigDecimal dollar;
    private BigDecimal high;
    private BigDecimal highdollar;
    private BigDecimal last;
    private BigDecimal low;
    private BigDecimal lowdollar;
    private BigDecimal open;

    @Column(name = "riserate")
    private BigDecimal riseRate;
    private BigDecimal sell;
    private BigDecimal selldollar;
    private String symbol;
    private BigDecimal vol;
}
