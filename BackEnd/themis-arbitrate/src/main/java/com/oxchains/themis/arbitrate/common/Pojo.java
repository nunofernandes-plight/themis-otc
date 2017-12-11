package com.oxchains.themis.arbitrate.common;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;

@Data
public class Pojo implements Serializable {
    private Long userId;
    private String id;
    private Integer successId;
    private Long noticeId;
    private String txId;
    private String content;
    private Integer status;
    private String amount;
    private BigDecimal money;
    private String fileName;
    private String thumbUrl;
    private Integer pageNum;
    private Integer pageSize;
    private MultipartFile[] multipartFile;
    public Integer getPageNum() {
        return pageNum;
    }

    @Override
    public String toString() {
        return "Pojo{" +
                "userId=" + userId +
                ", id='" + id + '\'' +
                ", successId=" + successId +
                ", noticeId=" + noticeId +
                ", txId='" + txId + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", amount='" + amount + '\'' +
                ", money=" + money +
                ", fileName='" + fileName + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", multipartFile=" + Arrays.toString(multipartFile) +
                '}';
    }
}
