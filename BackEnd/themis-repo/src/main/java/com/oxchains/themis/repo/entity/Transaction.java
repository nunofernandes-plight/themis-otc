package com.oxchains.themis.repo.entity;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2017-10-17 11:11
 * @name Transaction
 * @desc:
 */
@Entity
@Table(name = "tbl_biz_transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 35)
    private String fromAddress;

    @Column(length = 35)
    private String recvAddress;

    @Column(length = 35)
    private String p2shAddress;

    @Column(length = 1024)
    private String p2shRedeemScript;

    @Column(length = 1024)
    private String signTx;

    private String orderId;

    private Integer txStatus;

    private Double amount;

    private Double txFee;

    private Double minerFee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getRecvAddress() {
        return recvAddress;
    }

    public void setRecvAddress(String recvAddress) {
        this.recvAddress = recvAddress;
    }

    public String getP2shAddress() {
        return p2shAddress;
    }

    public void setP2shAddress(String p2shAddress) {
        this.p2shAddress = p2shAddress;
    }

    public String getP2shRedeemScript() {
        return p2shRedeemScript;
    }

    public void setP2shRedeemScript(String p2shRedeemScript) {
        this.p2shRedeemScript = p2shRedeemScript;
    }

    public String getSignTx() {
        return signTx;
    }

    public void setSignTx(String signTx) {
        this.signTx = signTx;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(Integer txStatus) {
        this.txStatus = txStatus;
    }

    private String utxoTxid;

    public String getUtxoTxid() {
        return utxoTxid;
    }

    public void setUtxoTxid(String utxoTxid) {
        this.utxoTxid = utxoTxid;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTxFee() {
        return txFee;
    }

    public void setTxFee(Double txFee) {
        this.txFee = txFee;
    }

    public Double getMinerFee() {
        return minerFee;
    }

    public void setMinerFee(Double minerFee) {
        this.minerFee = minerFee;
    }
}
