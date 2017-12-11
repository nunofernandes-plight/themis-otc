package com.oxchains.themis.user.service;

import com.oxchains.themis.repo.dao.TransactionDao;
import com.oxchains.themis.repo.entity.Transaction;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ccl
 * @time 2017-10-17 11:27
 * @name TransactionService
 * @desc:
 */
//@Transactional
@Service
public class TransactionService {
    @Resource
    private TransactionDao transactionDao;

    Transaction findByRecvAddress(String recvAddress) {
        return transactionDao.findByRecvAddress(recvAddress);
    }
}
