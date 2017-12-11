package com.oxchains.themis.notice.rest.dto;

import com.oxchains.themis.notice.domain.CNYDetail;
import lombok.Data;

import java.util.List;

/**
 * 所在地，货币类型，支付方式等DTO
 *
 * @author luoxuri
 * @create 2017-10-26 19:29
 **/
@Data
public class StatusDTO <T> {

    private Iterable<T> locationList;
    private Iterable<T> currencyList;
    private Iterable<T> searchTypeList;
    private Iterable<T> paymentList;

    private Iterable<T> bTCMarketList;
    private CNYDetail cnyDetailList;
}
