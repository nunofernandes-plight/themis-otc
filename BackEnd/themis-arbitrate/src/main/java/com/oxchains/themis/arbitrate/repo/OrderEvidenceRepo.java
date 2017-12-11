package com.oxchains.themis.arbitrate.repo;

import com.oxchains.themis.arbitrate.entity.OrderEvidence;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEvidenceRepo extends CrudRepository<OrderEvidence,Long>{
    OrderEvidence findByOrderId(String orderId);
}
