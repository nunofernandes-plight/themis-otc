package com.oxchains.themis.notice.dao;

import com.oxchains.themis.repo.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author luoxuri
 * @create 2017-10-25 10:21
 **/
@Repository
public interface NoticeDao extends CrudRepository<Notice,Long>, PagingAndSortingRepository<Notice, Long> {

    // 发布公告需要提前判断的接口

    List<Notice> findByUserIdAndNoticeType(Long userId, Long noticeType);
    List<Notice> findByUserIdAndNoticeTypeAndTxStatus(Long userId, Long noticeType, Integer txStatus);

    // 查询自己的公告

    Page<Notice> findByUserIdAndNoticeTypeAndTxStatus(Long userId, Long noticeType, Integer txStatus, Pageable pageable);


    // 搜索广告用到的接口(未分页)

    List<Notice> findByNoticeType(Long noticeType);
    List<Notice> findByLocationAndNoticeType(Long location, Long noticeType);
    List<Notice> findByCurrencyAndNoticeType(Long currency, Long noticeType);
    List<Notice> findByPayTypeAndNoticeType(Long payType, Long noticeType);
    List<Notice> findByLocationAndCurrencyAndNoticeType(Long location, Long currency, Long noticeType);
    List<Notice> findByLocationAndPayTypeAndNoticeType(Long location, Long payType, Long noticeType);
    List<Notice> findByCurrencyAndPayTypeAndNoticeType(Long currency, Long payType, Long noticeType);
    List<Notice> findByLocationAndCurrencyAndPayTypeAndNoticeType(Long location, Long currency, Long payType, Long noticeType);

    // 查询所有未完成订单

    List<Notice> findByTxStatus(Integer txStatus);

    // 分页搜索公告

    Page<Notice> findByNoticeTypeAndTxStatus(Long noticeType, Integer txStatus, Pageable pageable);
    Page<Notice> findByLocationAndNoticeTypeAndTxStatus(Long location, Long noticeType, Integer txStatus, Pageable pageable);
    Page<Notice> findByCurrencyAndNoticeTypeAndTxStatus(Long currency, Long noticeType, Integer txStatus, Pageable pageable);
    Page<Notice> findByPayTypeAndNoticeTypeAndTxStatus(Long payType, Long noticeType, Integer txStatus, Pageable pageable);
    Page<Notice> findByLocationAndCurrencyAndNoticeTypeAndTxStatus(Long location, Long currency, Long noticeType, Integer txStatus, Pageable pageable);
    Page<Notice> findByLocationAndPayTypeAndNoticeTypeAndTxStatus(Long location, Long payType, Long noticeType, Integer txStatus, Pageable pageable);
    Page<Notice> findByCurrencyAndPayTypeAndNoticeTypeAndTxStatus(Long currency, Long payType, Long noticeType, Integer txStatus, Pageable pageable);
    Page<Notice> findByLocationAndCurrencyAndPayTypeAndNoticeTypeAndTxStatus(Long location, Long currency, Long payType, Long noticeType, Integer txStatus, Pageable pageable);

    // 首页查询部分数据的接口

    List<Notice> findByNoticeTypeAndTxStatus(Long noticeType, Integer txStatus);

    // 搜用户,未使用

    Page<Notice> findByLoginnameAndTxStatusAndNoticeType(String loginname, Integer txStatus, Long noticeType, Pageable pageable);
}