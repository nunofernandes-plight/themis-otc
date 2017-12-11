package com.oxchains.themis.notice.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author luoxuri
 * @create 2017-10-27 10:37
 **/
@Entity
@Table(name = "tbl_biz_searchtype")
@Data
public class SearchType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
}
