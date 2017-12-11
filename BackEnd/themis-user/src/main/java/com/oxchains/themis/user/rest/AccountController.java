package com.oxchains.themis.user.rest;

import com.oxchains.themis.common.model.OrdersKeyAmount;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.user.service.AccountService;
import com.oxchains.themis.user.service.BitcoinService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author ccl
 * @time 2017-10-16 17:44
 * @name AccountController
 * @desc:
 */
@RestController
@RequestMapping(value = "/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @Resource
    private BitcoinService bitcoinService;

    /**
     * 查询余额
     * @param accountName
     * @return
     */
    @GetMapping(value = "/balance/{accountName}")
    public RestResp getBalance(@PathVariable String accountName) {
        return RestResp.success(accountService.getBalance(accountName));
    }

    /**
     * 获取交易地址
     * @param accountName
     * @return
     */
    @GetMapping(value = "/address/{accountName}")
    public RestResp getNewAddress(@PathVariable String accountName) {
        return RestResp.success(accountService.getAddress(accountName));
    }

    /**
     * 转账(协商地址)
     * @param accountName
     * @param recvAddress
     * @param amount
     * @param pubKeys
     * @param nRequired
     * @return
     */
    @PostMapping(value = "/transfer/{accountName}")
    public RestResp transferAccounts(@PathVariable String accountName, String recvAddress,double amount,String pubKeys,int nRequired) {
        return accountService.createTransaction(accountName,recvAddress,amount,Arrays.asList(pubKeys.split(",")),nRequired);
    }

    /**
     * 确认交易
     * @param accountName
     * @param recvAddress
     * @param amount
     * @param prvKeys
     * @return
     */
    @PostMapping(value = "/confirm/{accountName}")
    public RestResp confirmTransaction(@PathVariable String accountName, String recvAddress,double amount,String prvKeys) {
        return accountService.confirmTransaction(recvAddress,amount,Arrays.asList(prvKeys.split(",")),1);
    }

    /**
     * 取消交易
     * @param accountName
     * @param recvAddress
     * @param amount
     * @param prvKeys
     * @return
     */
    @PostMapping(value = "/cancel/{accountName}")
    public RestResp cancelTransaction(@PathVariable String accountName, String recvAddress,double amount,String prvKeys) {
        return accountService.confirmTransaction(recvAddress,amount,Arrays.asList(prvKeys.split(",")),0);
    }

    /**
     * 获取公私钥
     * @param accountName
     * @return
     */
    @GetMapping(value = "/key/{accountName}")
    public RestResp getKeys(@PathVariable String accountName){
        return accountService.getKeys(accountName);
    }

    /**
     * 获取公私钥
     * @return
     */
    @GetMapping(value = "/keys")
    public RestResp getKeys(){
        return bitcoinService.getKeys();
    }

    @PostMapping(value = "/transaction")
    public RestResp createTransaction(String fromAddress,String txId,String prvKey,String toAddress,String pubKeys,double amount,int required){
        return bitcoinService.createTransaction(fromAddress,txId,prvKey,toAddress,amount,Arrays.asList(pubKeys.split(",")),required);
    }
    @PostMapping(value = "/finish")
    public RestResp finishTransaction(String fromAddress,String toAddress,String prvKeys,double amount){
        return bitcoinService.confirmTransaction(toAddress,amount,Arrays.asList(prvKeys.split(",")),1);
    }

    @PostMapping(value = "/p2sh")
    public String getScriptHash(@RequestBody OrdersKeyAmount param){//String orderId,String pubKeys,Double amount
        return JsonUtil.toJson(bitcoinService.getScriptHash(param.getOrderId(),Arrays.asList(param.getPubKeys().split(",")),param.getAmount()));
    }

    @PostMapping(value = "/{orderId}")
    public String addTxid(@PathVariable String orderId,@RequestBody OrdersKeyAmount keyAmount){
        return JsonUtil.toJson(bitcoinService.addTxid(orderId,keyAmount.getTxId()));
    }

    @GetMapping(value = "/{orderId}")
    public String getStatus(@PathVariable String orderId){
        return JsonUtil.toJson(bitcoinService.getTransactionStatus(orderId));
    }

    @PostMapping(value = "/p2ur")
    public String payToUser(@RequestBody OrdersKeyAmount param){
        /*return JsonUtil.toJson(bitcoinService.payToUser(param.getOrderId(),param.getRecvAddress(),Arrays.asList(param.getPrvKeys().split(",")),param.getAmount()));*/
        return JsonUtil.toJson(bitcoinService.payToUserWithFees(param.getOrderId(),param.getRecvAddress(),Arrays.asList(param.getPrvKeys().split(",")),param.getAmount()));
    }

    @GetMapping(value = "/transaction/{orderId}")
    public RestResp getTransaction(@PathVariable String orderId){
        return bitcoinService.getTransaction(orderId);
    }
}
