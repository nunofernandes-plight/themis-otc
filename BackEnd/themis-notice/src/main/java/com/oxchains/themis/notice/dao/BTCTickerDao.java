package com.oxchains.themis.notice.dao;

import com.oxchains.themis.notice.domain.BTCTicker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author luoxuri
 * @create 2017-10-25 10:21
 **/
@Repository
public interface BTCTickerDao extends CrudRepository<BTCTicker, Long>{

    List<BTCTicker> findBySymbol(String symbol);

    Iterable<BTCTicker> findBTCTickerBySymbol(String symbol);
}
