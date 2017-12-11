package com.oxchains.themis.repo.dao;

import com.oxchains.themis.repo.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ccl
 * @time 2017-10-31 10:19
 * @nameOrderDao
 * @desc:
 */
@Repository
public interface OrderDao extends CrudRepository<Order,Long> {
    List<Order> findByBuyerIdOrSellerId(Long buyId, Long sellerId);
    int countByBuyerIdOrSellerId(Long buyId,Long sellerId);

    Order findById(String orderId);
}
