package com.oxchains.themis.notice.rest.dto;

import lombok.Data;

import java.util.List;

/**
 * @author luoxuri
 * @create 2017-10-27 17:42
 **/
@Data
public class HomeDTO <T> {

    private List<T> userTxDetailLst;

    private List<T> noticeList;

}
