package com.oxchains.themis.notice.listener;

import com.oxchains.themis.common.util.HttpUtils;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.common.util.VerifyNumUtil;
import com.oxchains.themis.notice.dao.BlockChainInfoDao;
import com.oxchains.themis.notice.dao.CNYDetailDao;
import com.oxchains.themis.notice.domain.BlockChainInfo;
import com.oxchains.themis.notice.domain.CNYDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 定时获取比特币价格的监听器 （获取来源：blockchain.info）
 * @author luoxuri
 * @create 2017-11-02 10:57
 **/
@Component
@Transactional
public class BlockChainInfoListener {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Resource private CNYDetailDao cnyDetailDao;
    @Resource private BlockChainInfoDao blockChainInfoDao;

    /**
     * 调度器
     * 每间隔 10 分钟执行一次
     *
     * 如果行情获取失败，就return
     * 如果行情获取成功，就保存，且数据库只保存一条btc-cny的信息，新得信息就update
     */
    @Scheduled(fixedRate = 1000 * 600)
    public void blockChainInfoListener(){
        try {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            LOG.info("Timed tasks begin to run：{} BTC market", currentTime);

            String url = "https://blockchain.info/ticker";
            String result = HttpUtils.sendGet(url);
            BlockChainInfo blockChainInfo = (BlockChainInfo) JsonUtil.fromJson(result, BlockChainInfo.class);
            LOG.info("BTC market: {}", blockChainInfo);
            if (null != blockChainInfo){
                String last = blockChainInfo.getCNY().getLast();
                String buy = blockChainInfo.getCNY().getBuy();
                String sell = blockChainInfo.getCNY().getSell();

                List<BlockChainInfo> bciList = blockChainInfoDao.findBySymbol("¥");
                CNYDetail cnyDetail = cnyDetailDao.findBySymbol("¥");
                if (bciList.size() != 0 && cnyDetail != null){
                    if (VerifyNumUtil.isNumber(last) && VerifyNumUtil.isNumber(buy) && VerifyNumUtil.isNumber(sell)){
                        for (BlockChainInfo b : bciList) {
                            b.setSaveTime(currentTime);
                            b.setSymbol("¥");
                            blockChainInfoDao.save(b);
                        }
                        cnyDetail.setSaveTime(currentTime);
                        cnyDetail.setBuy(blockChainInfo.getCNY().getBuy());
                        cnyDetail.setLast(blockChainInfo.getCNY().getLast());
                        cnyDetail.setSell(blockChainInfo.getCNY().getSell());
                        cnyDetail.setSymbol(blockChainInfo.getCNY().getSymbol());
                        cnyDetailDao.save(cnyDetail);
                        LOG.info("比特币价格获取正常");
                    }else {
                        LOG.error("比特币价格获取异常，原因：有非法字符");
                        return;
                    }
                }else {
                    if (VerifyNumUtil.isNumber(last) && VerifyNumUtil.isNumber(buy) && VerifyNumUtil.isNumber(sell)){
                        blockChainInfo.setSymbol("¥");
                        blockChainInfo.setSaveTime(currentTime);
                        blockChainInfoDao.save(blockChainInfo);

                        blockChainInfo.getCNY().setSaveTime(currentTime);
                        cnyDetailDao.save(blockChainInfo.getCNY());
                        LOG.info("比特币价格获取正常");
                    }else {
                        LOG.error("比特币价格获取异常，原因：有非法字符");
                        return;
                    }

                }
            }else {
                LOG.error("query BTC market failed");
                return;

            }
            LOG.info("This timed tasks has been completed");
        }catch (Exception e){
            LOG.error("定时任务：获取比特币价格异常", e);
        }
    }

}
