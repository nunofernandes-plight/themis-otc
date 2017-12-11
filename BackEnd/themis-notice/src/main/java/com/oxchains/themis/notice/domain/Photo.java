package com.oxchains.themis.notice.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * 默认头像
 * @author luoxuri
 * @create 2017-11-06 11:52
 **/
@Entity
@Data
@Table(name = "photo")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String photo;
}
