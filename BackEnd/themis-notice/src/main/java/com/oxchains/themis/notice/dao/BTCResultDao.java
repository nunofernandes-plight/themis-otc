package com.oxchains.themis.notice.dao;

import com.oxchains.themis.notice.domain.BTCResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author luoxuri
 * @create 2017-10-25 9:39
 **/
@Repository
public interface BTCResultDao extends CrudRepository<BTCResult,Long> {

    List<BTCResult> findByIsSuc(String isSuccess);
}
