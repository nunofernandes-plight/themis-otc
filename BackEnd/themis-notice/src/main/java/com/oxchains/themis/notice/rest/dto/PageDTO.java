package com.oxchains.themis.notice.rest.dto;

import lombok.Data;

import java.util.List;

/**
 * @author luoxuri
 * @create 2017-10-26 10:53
 **/
@Data
public class PageDTO <T> {
    private List<T> pageList;

    private Integer totalPage;

    private Integer currentPage;

    private Integer pageSize;

    private Long rowCount;
}
