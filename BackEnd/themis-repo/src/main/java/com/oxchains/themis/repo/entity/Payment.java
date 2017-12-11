package com.oxchains.themis.repo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by huohuo on 2017/10/23.
 * @author huohuo
 */
@Entity
@Table(name = "tbl_biz_payment")
@Data
public class Payment {
    @Id
    private Long id;
    private String paymentName;
}
