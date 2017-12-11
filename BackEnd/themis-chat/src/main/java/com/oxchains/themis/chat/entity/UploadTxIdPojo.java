package com.oxchains.themis.chat.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by xuqi on 2017/12/4.
 */
@Data
public class UploadTxIdPojo {
    //订单id
    private String id;
    private String txId;
    private Integer uploadType; //上传形式 1 pc端客户手填，2 移动端 自动
}
