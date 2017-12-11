package com.oxchains.themis.notice.dao;

import com.oxchains.themis.notice.domain.Photo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author luoxuri
 * @create 2017-11-06 11:54
 **/
@Repository
public interface PhotoDao extends CrudRepository<Photo, Long> {
}
