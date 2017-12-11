package com.oxchains.themis.common.model;

/**
 * Created by huohuo on 2017/10/26.
 */
public class OrdersKeyAmount {
    private String orderId;
    private String pubKeys;
    private Double amount;
    private String recvAddress;
    private String prvKeys;
    private String txId;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public OrdersKeyAmount(String orderId, String prvKeys, Double amount, String recvAddress) {
        this.orderId = orderId;
        this.prvKeys = prvKeys;
        this.amount = amount;
        this.recvAddress = recvAddress;
    }

    public OrdersKeyAmount() {
    }

    public String getRecvAddress() {
        return recvAddress;
    }

    public void setRecvAddress(String recvAddress) {
        this.recvAddress = recvAddress;
    }

    public OrdersKeyAmount(String orderId, String pubKeys, Double amount) {
        this.orderId = orderId;
        this.pubKeys = pubKeys;
        this.amount = amount;
    }

    public String getPubKeys() {
        return pubKeys;
    }

    public void setPubKeys(String pubKeys) {
        this.pubKeys = pubKeys;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "OrdersKeyAmount{" +
                "orderId='" + orderId + '\'' +
                ", pubKeys='" + pubKeys + '\'' +
                ", amount=" + amount +
                ", recvAddress='" + recvAddress + '\'' +
                ", prvKeys='" + prvKeys + '\'' +
                '}';
    }

    public String getPrvKeys() {
        return prvKeys;
    }

    public void setPrvKeys(String prvKeys) {
        this.prvKeys = prvKeys;
    }
}
