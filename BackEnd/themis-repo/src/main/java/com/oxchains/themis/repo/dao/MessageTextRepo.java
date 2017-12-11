package com.oxchains.themis.repo.dao;

import com.oxchains.themis.repo.entity.MessageText;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by xuqi on 2017/11/7.
 * @author huohuo
 */
@Repository
public interface MessageTextRepo extends CrudRepository<MessageText,Long>{
}
