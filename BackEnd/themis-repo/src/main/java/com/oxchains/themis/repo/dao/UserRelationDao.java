package com.oxchains.themis.repo.dao;

import com.oxchains.themis.repo.entity.UserRelation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ccl
 * @time 2017-11-06 9:58
 * @name UserRelationDao
 * @desc:
 */
@Repository
public interface UserRelationDao extends CrudRepository<UserRelation,Long> {
    /**
     * 查询用户信任关系
     * @param id
     * @return
     */
    Page<UserRelation> findByFromUserIdAndStatus(Long id, Integer status,Pageable pager);

    /**
     * 信任人数
     * @param id
     * @return
     */
    int countByFromUserIdAndStatus(Long id,Integer status);

    /**
     * 查询用户被信任关系
     * @param id
     * @return
     */
    Page<UserRelation> findByToUserIdAndStatus(Long id, Integer status,Pageable pager);

    UserRelation findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
}
