package com.oxchains.themis.notice.dao;

import com.oxchains.themis.notice.domain.BTCMarket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author luoxuri
 * @create 2017-10-25 10:20
 **/
@Repository
public interface BTCMarketDao extends CrudRepository<BTCMarket, Long>{

    List<BTCMarket> findBySymbol(String symbol);
}
