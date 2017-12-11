package com.oxchains.themis.notice.dao;

import com.oxchains.themis.notice.domain.SearchType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author luoxuri
 * @create 2017-10-27 10:39
 **/
@Repository
public interface SearchTypeDao extends CrudRepository<SearchType,Long> {
}
