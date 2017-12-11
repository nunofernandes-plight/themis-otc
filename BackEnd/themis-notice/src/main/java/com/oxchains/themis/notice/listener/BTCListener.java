package com.oxchains.themis.notice.listener;

import com.oxchains.themis.common.util.HttpUtils;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.notice.dao.BTCMarketDao;
import com.oxchains.themis.notice.dao.BTCResultDao;
import com.oxchains.themis.notice.dao.BTCTickerDao;
import com.oxchains.themis.notice.domain.BTCMarket;
import com.oxchains.themis.notice.domain.BTCResult;
import com.oxchains.themis.notice.domain.BTCTicker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author luoxuri
 * @create 2017-10-24 19:06
 **/
@Component
public class BTCListener {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Resource private BTCResultDao btcResultDao;
    @Resource private BTCMarketDao btcMarketDao;
    @Resource private BTCTickerDao btcTickerDao;

    /**
     * 调度器
     * 每间隔 12 分钟执行一次
     *
     * 如果行情获取失败，就return
     * 如果行情获取成功，就保存，且数据库只保存一条btc-cny的信息，新得信息就update
     */
//    @Scheduled(fixedRate = 1000 * 720)
    public void queryBTCMarket(){
        try {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            LOG.info("Timed tasks begin to run：{} BTC market", currentTime);

            String url = "https://www.btc123.com/api/getTicker?symbol=huobibtccny";
            String result = HttpUtils.sendGet(url);
            BTCResult btcResult = (BTCResult) JsonUtil.fromJson(result, BTCResult.class);

            LOG.info("BTC market: {}", btcResult);
            LOG.info("This timed tasks has been completed");

            List<BTCResult> btcResultList = btcResultDao.findByIsSuc("true");
            List<BTCMarket> btcMarketList = btcMarketDao.findBySymbol("huobibtccny");
            List<BTCTicker> btcTickerList = btcTickerDao.findBySymbol("btccny");
            if(!btcResultList.isEmpty() && !btcMarketList.isEmpty() && !btcTickerList.isEmpty()){
                for (int i=0; i<btcResultList.size(); i++){
                    BTCResult btcResultInfo = btcResultList.get(i);
                    btcResultInfo.setDes(btcResult.getDes());
                    btcResultInfo.setIsSuc(btcResult.getIsSuc());
                    btcResultInfo.setDatas(btcResult.getDatas());
                    btcResultDao.save(btcResultInfo);
                }
                for (int i=0; i<btcMarketList.size(); i++){
                    BTCMarket btcMarketInfo = btcMarketList.get(i);
                    btcMarketInfo.setCName(btcResult.getDatas().getCName());
                    btcMarketInfo.setCoinId(btcResult.getDatas().getCoinId());
                    btcMarketInfo.setCoinName(btcResult.getDatas().getCoinName());
                    btcMarketInfo.setCoinSign(btcResult.getDatas().getCoinSign());
                    btcMarketInfo.setExeByRate(btcResult.getDatas().getExeByRate());
                    btcMarketInfo.setIsRecomm(btcResult.getDatas().getIsRecomm());
                    btcMarketInfo.setMarketValue(btcResult.getDatas().getMarketValue());
                    btcMarketInfo.setMoneyType(btcResult.getDatas().getMoneyType());
                    btcMarketInfo.setName(btcResult.getDatas().getName());
                    btcMarketInfo.setSymbol(btcResult.getDatas().getSymbol());
                    btcMarketInfo.setTime(btcResult.getDatas().getTime());
                    btcMarketInfo.setType(btcResult.getDatas().getType());
                    btcMarketInfo.setTicker(btcResult.getDatas().getTicker());
                    btcMarketDao.save(btcMarketInfo);
                }
                for (int i=0; i<btcTickerList.size(); i++){
                    BTCTicker btcTickerInfo = btcTickerList.get(i);
                    btcTickerInfo.setBuy(btcResult.getDatas().getTicker().getBuy());
                    btcTickerInfo.setBuydollar(btcResult.getDatas().getTicker().getBuydollar());
                    btcTickerInfo.setDollar(btcResult.getDatas().getTicker().getDollar());
                    btcTickerInfo.setHigh(btcResult.getDatas().getTicker().getHigh());
                    btcTickerInfo.setHighdollar(btcResult.getDatas().getTicker().getHighdollar());
                    btcTickerInfo.setLast(btcResult.getDatas().getTicker().getLast());
                    btcTickerInfo.setLow(btcResult.getDatas().getTicker().getLow());
                    btcTickerInfo.setLowdollar(btcResult.getDatas().getTicker().getLowdollar());
                    btcTickerInfo.setOpen(btcResult.getDatas().getTicker().getOpen());
                    btcTickerInfo.setRiseRate(btcResult.getDatas().getTicker().getRiseRate());
                    btcTickerInfo.setSell(btcResult.getDatas().getTicker().getSell());
                    btcTickerInfo.setSelldollar(btcResult.getDatas().getTicker().getSelldollar());
                    btcTickerInfo.setSymbol(btcResult.getDatas().getTicker().getSymbol());
                    btcTickerInfo.setVol(btcResult.getDatas().getTicker().getVol());
                    btcTickerDao.save(btcTickerInfo);
                }
            }else {
                if (btcResult.getIsSuc().equals("false")){
                    return;
                }else {
                    btcResultDao.save(btcResult);
                    btcMarketDao.save(btcResult.getDatas());
                    btcTickerDao.save(btcResult.getDatas().getTicker());
                }

            }
        }catch (Exception e){
            LOG.error("定时任务：获取BTC深度行情异常", e);
        }
    }
}
