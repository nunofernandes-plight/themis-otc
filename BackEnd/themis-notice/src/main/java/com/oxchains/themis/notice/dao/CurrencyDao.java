package com.oxchains.themis.notice.dao;

import com.oxchains.themis.notice.domain.Country;
import com.oxchains.themis.notice.domain.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author luoxuri
 * @create 2017-10-25 10:21
 **/
@Repository
public interface CurrencyDao extends CrudRepository<Currency, Long> {
}
