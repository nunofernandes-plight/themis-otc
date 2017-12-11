package com.oxchains.themis.notice.dao;

import com.oxchains.themis.notice.domain.CNYDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author luoxuri
 * @create 2017-11-02 13:36
 **/
@Repository
public interface CNYDetailDao extends CrudRepository<CNYDetail, Long> {

    CNYDetail findBySymbol(String symbol);
}
