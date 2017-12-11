package com.oxchains.themis.chat.entity;

import lombok.Data;

import javax.persistence.*;
/**
 * create by huohuo
 * @author huohuo
 */
@Entity
@Data
public class ChatContent {
    @Id
    private String id;
    private Integer senderId;
    private String chatContent;
    private String createTime;
    private String senderName;
    private Integer receiverId;
    private String chatId;
    private Integer msgType;
    private String status;
    private String orderId;
    @Transient
    private Integer healthType;
}
