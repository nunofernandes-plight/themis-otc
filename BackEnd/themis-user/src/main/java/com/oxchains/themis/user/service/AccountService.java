package com.oxchains.themis.user.service;

import com.oxchains.bitcoin.rpcclient.BitcoinJSONRPCClient;
import com.oxchains.bitcoin.rpcclient.BitcoindRpcClient;

import com.oxchains.themis.common.model.AddressKeys;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.ArithmeticUtils;
import com.oxchains.themis.repo.dao.TransactionDao;

import com.oxchains.themis.repo.entity.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @author oxchains
 * @time 2017-10-16 15:25
 * @name AccountService
 * @desc:
 */
@Slf4j
@Service
public class AccountService {
    private static BitcoinJSONRPCClient client = null;

    static {
        try {
            URL url = new URL("http://admin1:123@192.168.1.195:8332/");
            client = new BitcoinJSONRPCClient(url);
        } catch (MalformedURLException e) {
            log.error("链接BitcoinJSONRPCClient异常", e);
        }
    }

    private Map<String, String> pubKeyMap = new HashMap<>();
    private Map<String, String> prvKeyMap = new HashMap<>();

    private List<String> signPubKeys = new ArrayList<>();
    private List<String> signPrvKeys = new ArrayList<>();

    private String p2shAddress = null;
    private String p2shRedeemScript = null;
    private String signedTx = null;

    private String utxoTxid = null;
    private final int UTXO_VOUT = 0;
    private String utxoOutputScript = null;
    //private String RAW_TX = null;
    /**
     * 矿工费
     */
    private final double TX_FEE = 0.0001D;

    private BitcoindRpcClient.MultiSig multiSig = null;

    private BitcoindRpcClient.RawTransaction preRawTransaction = null;
    private BitcoindRpcClient.RawTransaction rawTransaction = null;

    @Resource
    private TransactionDao transactionDao;

    /**
     * Sign up for an account
     *
     * @param accountName
     * @return address
     */
    public String enrollAccount(String accountName) {
        return getAddress(accountName);
    }

    /**
     * get account's balance
     *
     * @param accountName
     * @return
     */
    public double getBalance(String accountName) {
        double balance = 0.0d;
        try {
            balance = client.getBalance(accountName);
            return balance;
        } catch (Exception e) {
            log.error("获取余额失败", e);
            return balance;
        }
    }

    public String getAddress(String accountName) {
        String address = null;
        try {
            address = client.getNewAddress(accountName);
            log.info(address);
            return address;
        } catch (Exception e) {
            log.error("获取地址异常", e);
            return address;
        }
    }

    /**
    * 1. 生成公钥/私钥
    * 2. 生成协商地址和赎回脚本
    * 3. 发送到协商地址
    * 4. 发送到接收地址 createrawtransaction return RAW_TX
    * 5.
    */
    public RestResp createTransaction(String accountName, String recvAddress, double amount, List<String> signPubKeys, int nRequired) {
        //getKeys(signAddresses);
        if (getBalance(accountName) < amount) {
            return RestResp.fail("余额不足!");
        }

        this.signPubKeys = signPubKeys;
        try {

            createScriptHash(nRequired);

            String fromAddress = getAddress(accountName);
            utxoTxid = sendToAddress(accountName, fromAddress, amount);

            sendToScriptHash(accountName, amount);

            Transaction order = new Transaction();
            order.setFromAddress(fromAddress);
            order.setP2shAddress(p2shAddress);
            order.setP2shRedeemScript(p2shRedeemScript);
            order.setSignTx(signedTx);
            order.setRecvAddress(recvAddress);
            order.setTxStatus(2);

            order = transactionDao.save(order);
            return RestResp.success(order);
        } catch (Exception e) {
            log.error("创建交易异常", e);
            return RestResp.fail("操作失败", e);
        }
    }

    public RestResp confirmTransaction(String recvAddress, double amount, List<String> signPrvKeys, int type) {//type 0:取消,1:确认
        this.signPrvKeys = signPrvKeys;

        try {
            Transaction order = transactionDao.findByRecvAddress(recvAddress);
            p2shAddress = order.getP2shAddress();
            p2shRedeemScript = order.getP2shRedeemScript();
            signedTx = order.getSignTx();
            rawTransaction = client.decodeRawTransaction(order.getSignTx());
            if(type == 0){
                sendToUser(order.getFromAddress(), amount);
                order.setTxStatus(0);
                transactionDao.save(order);
                return RestResp.success("交易取消成功");
            }else {
                sendToUser(recvAddress, amount);
                order.setTxStatus(1);
                transactionDao.save(order);
                return RestResp.success("交易成功");
            }
        } catch (Exception e) {
            log.error("确认交易失败", e);
            return RestResp.fail("确认交易异常", e);
        }
    }

    public RestResp getKeys(String accountName){
        List<String> addresses = client.getAddressesByAccount(accountName);
        List<AddressKeys> addressKeysList=new ArrayList<>();
        Iterator<String> it = addresses.iterator();
        while (it.hasNext()) {
           String address = it.next();
            String pubKey = getPublicKey(address);
            String prvKey = getPrivateKey(address);
            addressKeysList.add(new AddressKeys(address,pubKey,prvKey));
        }

        return RestResp.success(addressKeysList);

    }

    /**
     * get public/private keys
     *
     * @param addresses
     */
    private void getKeys(List<String> addresses) {
        Iterator<String> it = addresses.iterator();
        while (it.hasNext()) {
            String address = it.next();
            String pubKey = getPublicKey(address);
            String prvKey = getPrivateKey(address);
            pubKeyMap.put(address, pubKey);
            prvKeyMap.put(address, prvKey);
        }
    }

    /**
     * @param accountName
     * @param recvAddress
     * @param amount
     * @return txid
     */
    public String sendToAddress(String accountName, String recvAddress, double amount) {
        BitcoindRpcClient.TxOutput pOutputs = new BitcoindRpcClient.BasicTxOutput(recvAddress, amount);
        List<BitcoindRpcClient.TxOutput> list = new ArrayList<>();
        list.add(pOutputs);
        utxoTxid = client.sendMany(accountName, list);
        return utxoTxid;
    }

    @Deprecated
    public String sendToAddress(String address, double amount) {
        utxoTxid = client.sendToAddress(address, amount);
        return utxoTxid;
    }

    /**
     * create script hash
     *
     * @param nRequired
     */
    private void createScriptHash(int nRequired) {
        multiSig = client.createMultiSig(nRequired, signPubKeys);
        p2shAddress = multiSig.address();
        p2shRedeemScript = multiSig.redeemScript();
    }

    public void getRawTransactin(String utxoTxid) {
        preRawTransaction = client.getRawTransaction(utxoTxid);
        utxoOutputScript = preRawTransaction.vOut().get(0).scriptPubKey().hex();
    }

    /**
     * send to script hash address
     *
     * @param inputAmount
     */
    private void sendToScriptHash(String accountName, double inputAmount) {

        List<BitcoindRpcClient.TxInput> txInputs = new ArrayList<>();
        List<BitcoindRpcClient.TxOutput> txOutputs = new ArrayList<>();
        BitcoindRpcClient.TxInput txInput = new BitcoindRpcClient.ExtendedTxInput(utxoTxid, UTXO_VOUT);
        txInputs.add(txInput);
        inputAmount = ArithmeticUtils.minus(inputAmount, TX_FEE);
        BitcoindRpcClient.TxOutput txOutput = new BitcoindRpcClient.BasicTxOutput(p2shAddress, inputAmount);
        txOutputs.add(txOutput);
        String rawTx = client.createRawTransaction(txInputs, txOutputs);
        signedTx = client.signRawTransaction(rawTx);
        submitRawTransaction(signedTx);
    }

    /**
     * send to user
     *
     * @param recvAddress
     */
    private void sendToUser(String recvAddress, double inputAmount) {
        List<BitcoindRpcClient.TxInput> txInputs = new ArrayList<>();
        List<BitcoindRpcClient.TxOutput> txOutputs = new ArrayList<>();
        List<BitcoindRpcClient.RawTransaction.Out> outs = rawTransaction.vOut();
        BitcoindRpcClient.RawTransaction.Out.ScriptPubKey scriptPubKey = outs.get(0).scriptPubKey();
        BitcoindRpcClient.TxInput txInput = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), UTXO_VOUT, scriptPubKey.hex(), p2shRedeemScript, BigDecimal.valueOf(inputAmount));
        txInputs.add(txInput);
        BitcoindRpcClient.TxOutput txOutput = new BitcoindRpcClient.BasicTxOutput(recvAddress, ArithmeticUtils.minus(inputAmount, TX_FEE));
        txOutputs.add(txOutput);

        String rawTx = client.createRawTransaction(txInputs, txOutputs);

        List<BitcoindRpcClient.ExtendedTxInput> txInputs1 = new ArrayList<>();
        BitcoindRpcClient.ExtendedTxInput txInput1 = new BitcoindRpcClient.ExtendedTxInput(rawTransaction.txId(), UTXO_VOUT, scriptPubKey.hex(), p2shRedeemScript, BigDecimal.valueOf(inputAmount - TX_FEE));
        txInputs1.add(txInput1);
        String lastTx = client.signRawTransaction(rawTx, txInputs1, signPrvKeys);

        submitRawTransaction(lastTx);
    }

    private void getRawTransaction(String txId) {
        try {
            rawTransaction = client.getRawTransaction(txId);
        } catch (Exception e) {
            log.error("获取原始交易失败", e);
        }

    }

    public String getPublicKey(String address) {
        try {
            BitcoindRpcClient.AddressValidationResult result = client.validateAddress(address);
            return result.pubKey();
        } catch (Exception e) {
            log.error("获取公钥失败", e);
        }
        return null;
    }

    private String getPrivateKey(String address) {
        try {
            return client.dumpPrivKey(address);
        } catch (Exception e) {
            log.error("获取私钥失败", e);
        }
        return null;
    }

    private String submitRawTransaction(String hex) {
        String result = null;

        return client.sendRawTransaction(hex);
    }
}
