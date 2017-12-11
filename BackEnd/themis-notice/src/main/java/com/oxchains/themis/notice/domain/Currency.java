package com.oxchains.themis.notice.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by huohuo on 2017/10/23.
 * @author luoxuri
 */
@Entity
@Table(name = "tbl_biz_currency")
public class Currency {
    @Id
    private Long id;
    private String currency_name;
    private String currency_short;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public String getCurrency_short() {
        return currency_short;
    }

    public void setCurrency_short(String currency_short) {
        this.currency_short = currency_short;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", currency_name='" + currency_name + '\'' +
                ", currency_short='" + currency_short + '\'' +
                '}';
    }
}
