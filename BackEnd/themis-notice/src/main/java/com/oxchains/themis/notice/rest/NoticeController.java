package com.oxchains.themis.notice.rest;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.notice.common.NoticeConst;
import com.oxchains.themis.notice.service.NoticeService;
import com.oxchains.themis.repo.entity.Notice;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author luoxuri
 * @create 2017-10-25 10:21
 **/
@RestController
@RequestMapping(value = "/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    /**
     * 发布公告
     */
    @PostMapping(value = "/broadcast")
    public RestResp broadcastNotice(@RequestBody Notice notice){
        return noticeService.broadcastNotice(notice);
    }

    /**
     * 随机查询两条购买公告、两条出售公告
     */
    @GetMapping(value = "/query/random")
    public RestResp queryRandomNotice(){
        return noticeService.queryRandomNotice();
    }

    /**
     * 查询所有公告
     */
    @GetMapping(value = "/query/all")
    public RestResp queryAllNotice(){
        return noticeService.queryAllNotice();
    }

    /**
     * 根据交易状态查询自己的公告
     */
    @GetMapping(value = "/query/me2")
    public RestResp queryMeAllNotice(@RequestParam Long userId, @RequestParam Integer pageNum, @RequestParam Long noticeType, @RequestParam Integer txStatus){
        return noticeService.queryMeAllNotice(userId, pageNum, noticeType, txStatus);
    }

    /**
     * 实时获取(火币网)BTC价格
     */
    @GetMapping(value = "/query/BTCPrice")
    public RestResp queryBTCPrice(){
        return noticeService.queryBTCPrice();
    }

    /**
     * 实时获取(火币网)BTC行情信息
     */
    @GetMapping(value = "/query/BTCMarket")
    public RestResp queryBTCMarket(){
        return noticeService.queryBTCMarket();
    }

    /**
     * 实时获取BlockChain.info BTC 价格
     */
    @GetMapping(value = "/query/blockchain.info")
    public RestResp queryBlockChainInfo(){
        return noticeService.queryBlockChainInfo();
    }

    /**
     * 搜索购买公告
     */
    @PostMapping(value = "/search/page/buy")
    public RestResp searchBuyPage(@RequestBody Notice notice){
        if (null == notice.getSearchType()) {
            notice.setSearchType(NoticeConst.SearchType.ONE.getStatus());
        }
        /* 1 默认是搜公告 */
        if (notice.getSearchType().equals(NoticeConst.SearchType.ONE.getStatus())){
            return noticeService.searchBuyPage(notice);
        }else {
            return noticeService.searchBuyPage(notice);
        }
    }

    /**
     * 搜索出售公告
     */
    @PostMapping(value = "search/page/sell")
    public RestResp searchSellPage(@RequestBody Notice notice){
        if (null == notice.getSearchType()) {
            notice.setSearchType(NoticeConst.SearchType.ONE.getStatus());
        }
        /* 1 默认是搜公告 */
        if (notice.getSearchType().equals(NoticeConst.SearchType.ONE.getStatus())){
            return noticeService.searchSellPage(notice);
        }else {
            return noticeService.searchSellPage(notice);
        }
    }

    /**
     * 下架公告
     */
    @GetMapping(value = "/stop")
    public RestResp stopNotice(@RequestParam Long id){
        return noticeService.stopNotice(id);
    }

    /**
     * 根据Id查找广告
     */
    @GetMapping(value = "/query/noticeId/{id}")
    public String queryNoticeOne(@PathVariable Long id){
        return JsonUtil.toJson(noticeService.queryNoticeOne(id));
    }

    /**
     * 修改广告交易状态
     */
    @GetMapping(value = "/update/txStatus/{id}/{txStatus}")
    public String updateTxStatus(@PathVariable Long id, @PathVariable Integer txStatus){
        return JsonUtil.toJson(noticeService.updateTxStatus(id, txStatus));
    }

    /**
     * 状态列表
     */
    @GetMapping(value = "/query/statusKV")
    public RestResp queryStatusKV(){
        return noticeService.queryStatusKV();
    }

}
