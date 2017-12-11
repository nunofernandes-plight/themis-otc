package com.oxchains.themis.chat.entity;

import lombok.Data;

/**
 * Created by xuqi on 2017/12/7.
 */
@Data
public class SocketPojo {
    private Long userId;
    private Long partnerId;
    private Integer socketType;
    private String orderId;

}
