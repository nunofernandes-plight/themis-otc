package com.oxchains.themis.repo.dao;

import com.oxchains.themis.repo.entity.TokenKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ccl
 * @time 2017-11-08 13:41
 * @name TokenKeyDao
 * @desc:
 */

@Repository
public interface TokenKeyDao extends CrudRepository<TokenKey,Long>,java.io.Serializable {
}
