package com.oxchains.themis.message.rest.dto;

import lombok.Data;

import java.util.List;

/**
 * @author luoxuri
 * @create 2017-12-05 14:43
 **/
@Data
public class PageDTO <T> {
    /**
     * T 类型集合
     */
    private List<T> PageList;

    /**
     * 按照每页显示的数量返回的总页数
     */
    private Integer totalPage;

    /**
     * 当前显示第几页
     */
    private Integer PageNum;

    /**
     * 每页显示的条数
     */
    private Integer pageSize;

    /**
     * 返回数据总数量
     */
    private Long rowCount;
}
