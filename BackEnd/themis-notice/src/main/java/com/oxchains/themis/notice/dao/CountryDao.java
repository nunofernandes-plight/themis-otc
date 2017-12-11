package com.oxchains.themis.notice.dao;

import com.oxchains.themis.notice.domain.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author luoxuri
 * @create 2017-10-24 19:06
 **/
@Repository
public interface CountryDao extends CrudRepository<Country, Long> {
}
