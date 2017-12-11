package com.oxchains.themis.notice.service;

import com.oxchains.themis.common.constant.notice.NoticeConstants;
import com.oxchains.themis.common.constant.notice.NoticeTxStatus;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.ArithmeticUtils;
import com.oxchains.themis.notice.common.NoticeConst;
import com.oxchains.themis.notice.dao.*;
import com.oxchains.themis.notice.domain.*;
import com.oxchains.themis.notice.domain.Currency;
import com.oxchains.themis.notice.rest.dto.PageDTO;
import com.oxchains.themis.notice.rest.dto.StatusDTO;
import com.oxchains.themis.repo.dao.PaymentRepo;
import com.oxchains.themis.repo.dao.UserDao;
import com.oxchains.themis.repo.dao.UserTxDetailDao;
import com.oxchains.themis.repo.entity.*;
import com.oxchains.themis.repo.entity.UserTxDetail;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * @author luoxuri
 * @create 2017-10-25 10:21
 **/
@Service
@Transactional(rollbackFor=Exception.class)
public class NoticeService {

    private final Logger LOG = LoggerFactory.getLogger(NoticeService.class);

    @Resource private NoticeDao noticeDao;
    @Resource private BTCTickerDao btcTickerDao;
    @Resource private BTCResultDao btcResultDao;
    @Resource private BTCMarketDao btcMarketDao;
    @Resource private CNYDetailDao cnyDetailDao;
    @Resource private CountryDao countryDao;
    @Resource private CurrencyDao currencyDao;
    @Resource private PaymentRepo paymentDao;
    @Resource private SearchTypeDao searchTypeDao;
    @Resource private UserTxDetailDao userTxDetailDao;
    @Resource private UserDao userDao;
    @Value("${themis.user.default}") private String userDefaultImage;

    public RestResp broadcastNotice(Notice notice){
        try {
            // 判断用于信息是否完善（收货地址）
            Long userId = notice.getUserId();
            User user = userDao.findOne(userId);
            String firstAddress = user.getFirstAddress();
            if (StringUtils.isBlank(firstAddress)){
                return RestResp.fail("请完善用户信息：收货地址");
            }
            // 必填项判断
            if (null == notice.getNoticeType()){
                return RestResp.fail("请选择广告类型");
            }
            if (null == notice.getLocation()){
                return RestResp.fail("请选择所在地");
            }
            if (null == notice.getCurrency()){
                return RestResp.fail("请选择货币类型");
            }
            if (null == notice.getPremium()){
                return RestResp.fail("请填写溢价");
            }
            if (null == notice.getPrice()){
                return RestResp.fail("比特币价格获取失败，请联系管理员！");
            }
            if (null == notice.getMinTxLimit()){
                return RestResp.fail("请填写最小限额");
            }
            if (null == notice.getMaxTxLimit()){
                return RestResp.fail("请填写最大限额");
            }
            if (null == notice.getPayType()){
                return RestResp.fail("请选择收款/付款方式");
            }
            if (notice.getPremium() < 0){
                return RestResp.fail("溢价比例最小为：0");
            }
            if (notice.getMinTxLimit().doubleValue() < 0){
                return RestResp.fail("最小交易限额：0");
            }
            if (notice.getMaxTxLimit().doubleValue() > NoticeConstants.ONE_HUNDRED_MILLION){
                return RestResp.fail("最大交易限额：1亿");
            }
            if (ArithmeticUtils.minus(notice.getMaxTxLimit().doubleValue(), notice.getMinTxLimit().doubleValue()) < 0){
                return RestResp.fail("最大限额不能低于最小限额");
            }
            if (notice.getPrice().doubleValue() <= 0){
                return RestResp.fail("价格异常，请联系管理员");
            }
            // 选填项（最低价判断）
            if (notice.getMinPrice() != null){
                if (notice.getMinPrice().doubleValue() < 0){
                    return RestResp.fail("最低价最小为：0");
                }
                Double marketLow = notice.getPrice().doubleValue();
                Double minPrice = notice.getMinPrice().doubleValue();
                if (ArithmeticUtils.minus(marketLow, minPrice) < 0){
                    notice.setPrice(notice.getMinPrice());
                }
            }

            // 不能发布公告得判断
            List<Notice> noticeListUnDone = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(notice.getUserId(), notice.getNoticeType(), NoticeTxStatus.UNDONE_TX);
            if (noticeListUnDone.size() != 0){
                return RestResp.fail("已经有一条此类型公告");
            }

            String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            notice.setCreateTime(createTime);
            Notice n = noticeDao.save(notice);
            return RestResp.success("操作成功", n);
        }catch (Exception e){
            LOG.error("发布公告异常", e);
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryRandomNotice(){
        try {
            List<Notice> partList = new ArrayList<>();
            List<Notice> buyNoticeList = noticeDao.findByNoticeTypeAndTxStatus(NoticeConst.NoticeType.BUY.getStatus(), NoticeTxStatus.UNDONE_TX);
            List<Notice> sellNoticeList = noticeDao.findByNoticeTypeAndTxStatus(NoticeConst.NoticeType.SELL.getStatus(), NoticeTxStatus.UNDONE_TX);

            if (buyNoticeList.size() == 0 && sellNoticeList.size() == 0){
                return RestResp.success("没有数据", new ArrayList<>());
            }
            if (buyNoticeList.size() > NoticeConstants.TWO && sellNoticeList.size() > NoticeConstants.TWO){
                int buySize = getRandom(buyNoticeList);
                int sellSize = getRandom(sellNoticeList);
                List<Notice> subBuyList = getSubList(buyNoticeList, buySize);
                List<Notice> subSellList = getSubList(sellNoticeList, sellSize);
                for (int i = 0; i < subBuyList.size(); i++){ setUserTxDetail(subBuyList, i);}
                for (int i = 0; i < subSellList.size(); i++){setUserTxDetail(subSellList, i);}
                partList.addAll(subBuyList);
                partList.addAll(subSellList);
            }else if (buyNoticeList.size() <= NoticeConstants.TWO && sellNoticeList.size() > NoticeConstants.TWO){
                int sellSize = getRandom(sellNoticeList);
                List<Notice> subSellList = getSubList(sellNoticeList, sellSize);
                for (int i = 0; i < buyNoticeList.size(); i++){setUserTxDetail(buyNoticeList, i);}
                for (int i = 0; i < subSellList.size(); i++){setUserTxDetail(subSellList, i);}
                partList.addAll(buyNoticeList);
                partList.addAll(subSellList);
            }else if (buyNoticeList.size() > NoticeConstants.TWO && sellNoticeList.size() <= NoticeConstants.TWO){
                int buySize = getRandom(buyNoticeList);
                List<Notice> subBuyList = getSubList(buyNoticeList, buySize);
                for (int i = 0; i < subBuyList.size(); i++){setUserTxDetail(subBuyList, i);}
                for (int i = 0; i < sellNoticeList.size(); i++){setUserTxDetail(sellNoticeList, i);}
                partList.addAll(subBuyList);
                partList.addAll(sellNoticeList);
            }else {
                for (int i = 0; i < buyNoticeList.size(); i++){setUserTxDetail(buyNoticeList, i);}
                for (int i = 0; i < sellNoticeList.size(); i++){setUserTxDetail(sellNoticeList, i);}
                partList.addAll(buyNoticeList);
                partList.addAll(sellNoticeList);
            }
            return RestResp.success("操作成功", partList);
        }catch (Exception e){
            LOG.error("获取4条随机公告异常", e);

        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryAllNotice(){
        try {
            Iterable<Notice> it = noticeDao.findAll();
            if(it.iterator().hasNext()){
                return RestResp.success("操作成功", it);
            }else {
                return RestResp.fail("操作失败");
            }
        }catch (Exception e){
            LOG.error("查询所有公告异常", e);
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryMeAllNotice(Long userId, Integer pageNum, Long noticeType, Integer txStatus){
        try {
            List<Notice> resultList = new ArrayList<>();
            Pageable pageable = new PageRequest(pageNum - 1, 5, new Sort(Sort.Direction.ASC, "createTime"));
            Page<Notice> page = null;
            if (txStatus.equals(NoticeTxStatus.DONE_TX)){
                page = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(userId, noticeType, NoticeTxStatus.DONE_TX, pageable);
                Iterator<Notice> it = page.iterator();
                while (it.hasNext()){
                    resultList.add(it.next());
                }
            }else {
                List<Notice> unDoneNoticeList = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(userId, noticeType, NoticeTxStatus.UNDONE_TX);
                resultList.addAll(unDoneNoticeList);
            }
            PageDTO<Notice> pageDTO = new PageDTO<>();
            if (page == null){
                pageDTO.setCurrentPage(1);
                pageDTO.setPageSize(5);
                pageDTO.setRowCount((long)resultList.size());
                pageDTO.setTotalPage(1);
                pageDTO.setPageList(resultList);
            }else {
                pageDTO.setCurrentPage(pageNum);
                pageDTO.setPageSize(5);
                pageDTO.setRowCount(page.getTotalElements());
                pageDTO.setTotalPage(page.getTotalPages());
                pageDTO.setPageList(resultList);
            }
            return RestResp.success("操作成功", pageDTO);
        }catch (Exception e){
            LOG.error("查询我的公告异常", e);
        }
        return RestResp.fail("操作失败");
    }

    /**
     * 火币网API接口停止服务，获取行情失败
     */
    @Deprecated
    public RestResp queryBTCPrice(){
        try {
            List<BTCTicker> btcTickerList = btcTickerDao.findBySymbol("btccny");
            if (!btcTickerList.isEmpty()){
                return RestResp.success("操作成功", btcTickerList);
            }else {
                return RestResp.fail("操作失败");
            }
        }catch (Exception e){
            LOG.error("查询BTC价格异常", e);
        }
        return RestResp.fail("操作失败");
    }

    /**
     * 火币网API接口停止服务，获取行情失败
     */
    @Deprecated
    public RestResp queryBTCMarket(){
        try {
            List<BTCResult> btcResultList = btcResultDao.findByIsSuc("true");
            List<BTCMarket> btcMarketList = btcMarketDao.findBySymbol("huobibtccny");
            List<BTCTicker> btcTickerList = btcTickerDao.findBySymbol("btccny");
            BTCResult btcResult = null;
            BTCMarket btcMarket = null;
            BTCTicker btcTicker = null;
            for (int i = 0; i < btcResultList.size(); i++){
                btcResult = btcResultList.get(i);
            }
            for (int i = 0; i < btcMarketList.size(); i++){
                btcMarket = btcMarketList.get(i);
            }
            for (int i = 0; i < btcTickerList.size(); i++){
                btcTicker = btcTickerList.get(i);
            }
            btcMarket.setTicker(btcTicker);
            btcResult.setDatas(btcMarket);
            return RestResp.success("操作成功", btcResultList);
        }catch (Exception e){
            LOG.error("查询BTC深度行情异常", e);
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryBlockChainInfo(){
        try {
            CNYDetail cnyDetail = cnyDetailDao.findBySymbol("¥");
            if (cnyDetail != null){
                return RestResp.success("操作成功", cnyDetail);
            }else {
                return RestResp.fail("操作失败");
            }
        }catch (Exception e){
            LOG.error("获取BTC价格异常", e);
        }
        return RestResp.fail("操作失败");
    }

    public RestResp searchBuyPage(Notice notice){
        try {
            Long location = notice.getLocation();
            Long currency = notice.getCurrency();
            Long payType = notice.getPayType();
            Long noticeType = NoticeConst.NoticeType.SELL.getStatus();
            Integer pageNum = notice.getPageNum();

            Pageable pageable = new PageRequest(pageNum - 1, 5, new Sort(Sort.Direction.ASC, "createTime"));

            // 对所在地，货币类型，支付方式判断，可为null
            Page<Notice> page = null;
            if (null != location && null != currency && null != payType) {
                page = noticeDao.findByLocationAndCurrencyAndPayTypeAndNoticeTypeAndTxStatus(location, currency, payType, noticeType, NoticeTxStatus.UNDONE_TX, pageable);
            }else if (null != location && null != currency && null == payType) {
                page = noticeDao.findByLocationAndCurrencyAndNoticeTypeAndTxStatus(location, currency, noticeType, NoticeTxStatus.UNDONE_TX, pageable);
            } else if (null != location && null == currency && null != payType) {
                page = noticeDao.findByLocationAndPayTypeAndNoticeTypeAndTxStatus(location, payType, noticeType, NoticeTxStatus.UNDONE_TX, pageable);
            } else if (null == location && null != currency && null != payType) {
                page = noticeDao.findByCurrencyAndPayTypeAndNoticeTypeAndTxStatus(currency, payType, noticeType, NoticeTxStatus.UNDONE_TX, pageable);
            } else if (null != location && null == currency && null == payType) {
                page = noticeDao.findByLocationAndNoticeTypeAndTxStatus(location, noticeType, NoticeTxStatus.UNDONE_TX, pageable);
            } else if (null == location && null == currency && null != payType) {
                page = noticeDao.findByPayTypeAndNoticeTypeAndTxStatus(payType, noticeType, NoticeTxStatus.UNDONE_TX, pageable);
            } else if (null == location && null != currency && null == payType) {
                page = noticeDao.findByCurrencyAndNoticeTypeAndTxStatus(currency, noticeType, NoticeTxStatus.UNDONE_TX, pageable);
            } else if (null == location && null == currency && null == payType) {
                page = noticeDao.findByNoticeTypeAndTxStatus(noticeType, NoticeTxStatus.UNDONE_TX, pageable);
            }else {
                return RestResp.fail("操作失败");
            }

            List<Notice> resultList = new ArrayList<>();
            Iterator<Notice> it = page.iterator();
            while (it.hasNext()){
                resultList.add(it.next());
            }

            // 将好评度等值添加到list中返回
            for (int i = 0; i < resultList.size(); i++){
                Long userId = resultList.get(i).getUserId();
                UserTxDetail utdInfo = userTxDetailDao.findByUserId(userId);
                if (utdInfo == null){
                    resultList.get(i).setTxNum(0);
                    resultList.get(i).setTrustNum(0);
                    resultList.get(i).setGoodPercent(0);
                }else {
                    resultList.get(i).setTxNum(utdInfo.getTxNum());
                    resultList.get(i).setTrustNum(utdInfo.getBelieveNum());
                    double descTotal = ArithmeticUtils.plus(utdInfo.getGoodDesc(), utdInfo.getBadDesc());
                    double goodP;
                    if (descTotal == 0){
                        goodP = 0;
                    }else {
                        goodP = ArithmeticUtils.divide(utdInfo.getGoodDesc(), descTotal, 2);
                    }

                    resultList.get(i).setGoodPercent((int)(goodP * 100));
                }

            }

            // 将page相关参数设置到DTO中返回
            PageDTO<Notice> pageDTO = new PageDTO<>();
            pageDTO.setCurrentPage(pageNum);
            pageDTO.setPageSize(5);
            pageDTO.setRowCount(page.getTotalElements());
            pageDTO.setTotalPage(page.getTotalPages());
            pageDTO.setPageList(resultList);
            return RestResp.success("操作成功", pageDTO);
        }catch (Exception e){
            LOG.error("搜索购买公告异常", e);
        }
        return RestResp.fail("操作失败");
    }

    public RestResp searchSellPage(Notice notice){
        try {
            Long location = notice.getLocation();
            Long currency = notice.getCurrency();
            Long payType = notice.getPayType();
            Long noticeType = NoticeConst.NoticeType.BUY.getStatus();
            Integer pageNum = notice.getPageNum();

            Pageable pageable = new PageRequest(pageNum - 1, 5, new Sort(Sort.Direction.ASC, "createTime"));

            Page<Notice> page = null;
            if (null != location && null != currency && null != payType) {
                page = noticeDao.findByLocationAndCurrencyAndPayTypeAndNoticeTypeAndTxStatus(location, currency, payType, noticeType, NoticeTxStatus.UNDONE_TX, pageable);
            }else if (null != location && null != currency && null == payType) {
                page = noticeDao.findByLocationAndCurrencyAndNoticeTypeAndTxStatus(location, currency, noticeType, NoticeTxStatus.UNDONE_TX, pageable);
            } else if (null != location && null == currency && null != payType) {
                page = noticeDao.findByLocationAndPayTypeAndNoticeTypeAndTxStatus(location, payType, noticeType, NoticeTxStatus.UNDONE_TX, pageable);
            } else if (null == location && null != currency && null != payType) {
                page = noticeDao.findByCurrencyAndPayTypeAndNoticeTypeAndTxStatus(currency, payType, noticeType, NoticeTxStatus.UNDONE_TX, pageable);
            } else if (null != location && null == currency && null == payType) {
                page = noticeDao.findByLocationAndNoticeTypeAndTxStatus(location, noticeType, NoticeTxStatus.UNDONE_TX, pageable);
            } else if (null == location && null == currency && null != payType) {
                page = noticeDao.findByPayTypeAndNoticeTypeAndTxStatus(payType, noticeType, NoticeTxStatus.UNDONE_TX, pageable);
            } else if (null == location && null != currency && null == payType) {
                page = noticeDao.findByCurrencyAndNoticeTypeAndTxStatus(currency, noticeType, NoticeTxStatus.UNDONE_TX, pageable);
            } else if (null == location && null == currency && null == payType) {
                page = noticeDao.findByNoticeTypeAndTxStatus(noticeType, NoticeTxStatus.UNDONE_TX, pageable);
            }else {
                return RestResp.fail("操作失败");
            }

            List<Notice> resultList = new ArrayList<>();
            Iterator<Notice> it = page.iterator();
            while (it.hasNext()){
                resultList.add(it.next());
            }

            // 将好评度等值添加到list中返回
            for (int i = 0; i < resultList.size(); i++){
                Long userId = resultList.get(i).getUserId();
                UserTxDetail utdInfo = userTxDetailDao.findByUserId(userId);
                if (null == utdInfo){
                    resultList.get(i).setTxNum(0);
                    resultList.get(i).setTrustNum(0);
                    resultList.get(i).setGoodPercent(0);
                }else {
                    resultList.get(i).setTxNum(utdInfo.getTxNum());
                    resultList.get(i).setTrustNum(utdInfo.getBelieveNum());
                    double descTotal = ArithmeticUtils.plus(utdInfo.getGoodDesc(), utdInfo.getBadDesc());
                    double goodP;
                    if (descTotal == 0){
                        goodP = 0;
                    }else {
                        goodP = ArithmeticUtils.divide(utdInfo.getGoodDesc(), descTotal, 2);
                    }
                    resultList.get(i).setGoodPercent((int)(goodP * 100));
                }
            }

            PageDTO<Notice> pageDTO = new PageDTO<>();
            pageDTO.setCurrentPage(pageNum);
            pageDTO.setPageSize(5);
            pageDTO.setRowCount(page.getTotalElements());
            pageDTO.setTotalPage(page.getTotalPages());
            pageDTO.setPageList(resultList);
            return RestResp.success("操作成功", pageDTO);
        }catch (Exception e){
            LOG.error("搜索出售公告异常", e);
        }
        return RestResp.fail("操作失败");
    }

    public RestResp stopNotice(Long id){
        try {
            Notice noticeInfo = noticeDao.findOne(id);
            if (null == noticeInfo) {
                return RestResp.fail("操作失败");
            }
            if (noticeInfo.getTxStatus().equals(NoticeTxStatus.DONE_TX)) {
                return RestResp.fail("公告已下架");
            }
            List<Notice> noticeList = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(noticeInfo.getUserId(), noticeInfo.getNoticeType(), noticeInfo.getTxStatus());
            if (noticeList.size() == 0){
                return RestResp.fail("操作失败");
            }
            for (Notice n : noticeList) {
                n.setTxStatus(NoticeTxStatus.DONE_TX);
                noticeDao.save(n);
            }
            return RestResp.success("操作成功");
        }catch (Exception e){
            LOG.error("下架公告异常", e);
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryNoticeOne(Long id){
        try {
            Notice notice = noticeDao.findOne(id);
            return RestResp.success("操作成功", notice);
        }catch (Exception e){
            LOG.error("根据公告ID查找异常", e);
        }
        return RestResp.fail("操作失败");
    }

    public RestResp updateTxStatus(Long id, Integer txStatus){
        try {
            Notice notice = noticeDao.findOne(id);
            notice.setTxStatus(txStatus);
            Notice n = noticeDao.save(notice);
            return RestResp.success("操作成功", n);
        }catch (Exception e){
            LOG.error("修改公告交易状态异常", e);
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryStatusKV(){
        try {
            Iterable<Country> location = countryDao.findAll();
            Iterable<Currency> currency = currencyDao.findAll();
            Iterable<Payment> payment = paymentDao.findAll();
            Iterable<SearchType> searchType = searchTypeDao.findAll();
            Iterable<BTCTicker> btcTiker = btcTickerDao.findBTCTickerBySymbol("btccny");
            CNYDetail cnyDetail = cnyDetailDao.findBySymbol("¥");

            if (location.iterator().hasNext() && currency.iterator().hasNext() && payment.iterator().hasNext() && searchType.iterator().hasNext()){
                StatusDTO statusDTO = new StatusDTO<>();
                statusDTO.setLocationList(location);
                statusDTO.setCurrencyList(currency);
                statusDTO.setPaymentList(payment);
                statusDTO.setSearchTypeList(searchType);
                statusDTO.setBTCMarketList(btcTiker);
                statusDTO.setCnyDetailList(cnyDetail);
                return RestResp.success("操作成功", statusDTO);
            } else {
                return RestResp.fail("操作失败");
            }
        }catch (Exception e){
            LOG.error("查询状态异常", e);
        }
        return RestResp.fail("操作失败");
    }

    // =================================================================

    /**
     * 根据list大小-2获取一个随机数
     */
    private int getRandom(List list){
        return new Random().nextInt(list.size() - 2);
    }

    /**
     * 对list截取，获取size为2的新list
     */
    private List getSubList(List list, int size){
        return list.subList(size, size + 2);
    }

    /**
     * 设置用户交易详情数据
     */
    private void setUserTxDetail(List<Notice> subList, int i) {
        Long userId = subList.get(i).getUserId();
        UserTxDetail userTxDetail = userTxDetailDao.findByUserId(userId);
        // 查找头像名
        User user = userDao.findOne(userId);
        String imageName = user.getImage();

        if (null == imageName){
            subList.get(i).setImageName(userDefaultImage);
        } else {
            subList.get(i).setImageName(imageName);
        }

        if (null == userTxDetail){
            subList.get(i).setTxNum(0);
            subList.get(i).setTrustNum(0);
            subList.get(i).setTrustPercent(0);
        }else {
            subList.get(i).setTxNum(userTxDetail.getTxNum());
            subList.get(i).setTrustNum(userTxDetail.getBelieveNum());
            double trustP;
            if (userTxDetail.getTxNum() == 0){
                trustP = 0;
            }else {
                trustP = ArithmeticUtils.divide(userTxDetail.getBelieveNum(), userTxDetail.getTxNum(), 2);
            }
            subList.get(i).setTrustPercent((int) ArithmeticUtils.multiply(trustP, 100, 0));
        }

    }



}
