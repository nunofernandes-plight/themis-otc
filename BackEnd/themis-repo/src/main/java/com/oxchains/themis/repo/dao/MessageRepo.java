package com.oxchains.themis.repo.dao;

import com.oxchains.themis.repo.entity.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xuqi on 2017/11/7.
 * @author huohuo
 */
@Repository
public interface MessageRepo extends CrudRepository<Message,Long> {
    List<Message> findByIdAndMessageTextId(Long id, Long textid);
}
