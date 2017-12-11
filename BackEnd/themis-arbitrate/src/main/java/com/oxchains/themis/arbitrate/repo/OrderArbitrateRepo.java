package com.oxchains.themis.arbitrate.repo;

import com.oxchains.themis.repo.entity.OrderArbitrate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderArbitrateRepo extends CrudRepository<OrderArbitrate,Long> {
    Page<OrderArbitrate> findByUserIdAndAndStatusIsNot(Long userId, Integer status, Pageable pageable);
    OrderArbitrate findByUserIdAndOrderId(Long id, String orderId);
    List<OrderArbitrate> findByOrderId(String orderId);
    List<OrderArbitrate> findByOrOrderIdAndStatus(String id, Integer status);

}
