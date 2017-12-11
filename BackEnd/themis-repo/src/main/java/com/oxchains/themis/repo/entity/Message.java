package com.oxchains.themis.repo.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 站内信
 *
 * @author luoxuri
 * @create 2017-11-06 14:42
 **/
@Entity
@Data
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;        // 编号

    private Long receiverId;     // 接受者编号

    private Long messageTextId; // 站内信编号

    private Integer readStatus; // 站内信的查看状态 1.未读 2.已读 3.删除

    private Integer messageType;   // 信息类型 1.global(系统消息) 2.public(公告) 3.private(私信)

    @Transient
    private MessageText messageText;

    public Message(Long receiverId, Long messageTextId, Integer readStatus, Integer messageType) {
        this.receiverId = receiverId;
        this.messageTextId = messageTextId;
        this.readStatus = readStatus;
        this.messageType = messageType;
    }

    public Message() {
    }
}
