package com.oxchains.themis.user.domain;

import com.oxchains.themis.repo.entity.UserRelation;
import com.oxchains.themis.repo.entity.UserTxDetail;

/**
 * @author ccl
 * @time 2017-11-06 10:32
 * @name UserTrust
 * @desc:
 */
public class UserTrust {

    private Long fromUserId;
    private Long toUserId;
    private String fromUserName;
    private String toUserName;

    /**
     * 交易次数
     */
    private Integer txNum;
    /**
     * 好评次数
     */
    private Integer goodDesc;
    /**
     * 差评次数
     */
    private Integer badDesc;
    /**
     * 第一次购买时间
     */
    private String firstBuyTime;
    /**
     * 信任次数
     */
    private Integer believeNum;
    private Double buyAmount;
    private Double sellAmount;

    /**
     * 交易次数1
     */
    private Integer txToNum;

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public Integer getTxNum() {
        return txNum;
    }

    public void setTxNum(Integer txNum) {
        this.txNum = txNum;
    }

    public Integer getGoodDesc() {
        return goodDesc;
    }

    public void setGoodDesc(Integer goodDesc) {
        this.goodDesc = goodDesc;
    }

    public Integer getBadDesc() {
        return badDesc;
    }

    public void setBadDesc(Integer badDesc) {
        this.badDesc = badDesc;
    }

    public String getFirstBuyTime() {
        return firstBuyTime;
    }

    public void setFirstBuyTime(String firstBuyTime) {
        this.firstBuyTime = firstBuyTime;
    }

    public Integer getBelieveNum() {
        return believeNum;
    }

    public void setBelieveNum(Integer believeNum) {
        this.believeNum = believeNum;
    }

    public Double getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(Double buyAmount) {
        this.buyAmount = buyAmount;
    }

    public Double getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(Double sellAmount) {
        this.sellAmount = sellAmount;
    }

    public Integer getTxToNum() {
        return txToNum;
    }

    public void setTxToNum(Integer txToNum) {
        this.txToNum = txToNum;
    }
}
