package com.oxchains.basicService.files.entity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by huohuo on 2017/12/1.
 */
@Data
@Entity(name = "file_infos")
public class FileInfos implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Transient
    private byte[] file;    //文件本身
    private String filename; //初始文件名
    private String tfsFilename; //文件系统中的文件名
    private Integer length; //文件大小
    private Integer appKey; //应用key
    private Long userId;   //用户编号
    private String fileFormat; //文件格式

}
