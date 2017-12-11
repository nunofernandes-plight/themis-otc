package com.oxchains.themis.repo.dao;

import com.oxchains.themis.repo.entity.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ccl
 * @time 2017-10-17 11:27
 * @name TransactionDao
 * @desc:
 */
@Repository
public interface TransactionDao extends CrudRepository<Transaction,Long> {
    /**
     * find by recv address
     * @param recvAddress
     * @return
     */
    Transaction findByRecvAddress(String recvAddress);

    /**
     * find by orderId
     * @param orderId
     * @return
     */
    Transaction findByOrderId(String orderId);

    Transaction findByUtxoTxid(String utxoTxid);
}
