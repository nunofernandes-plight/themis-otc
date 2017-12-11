package com.oxchains.themis.user.bitcoin;

import com.oxchains.themis.common.util.ArithmeticUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * @author ccl
 * @time 2017-10-13 17:43
 * @name BitcoinConfig
 * @desc:
 */
@Component
public class BitcoinConfig extends AbstractConfig{

    private static String url;
    private static String port;
    private static String username;
    private static String password;

    private static String feeRate;
    private static String maxFee;
    private static String minFee;

    private BitcoinConfig(){}
    public static String getUrlString(){
        return "http://"+username+":"+password+"@"+url+":"+port+"/";
    }

    public static double getFeeRate(){
        return Double.valueOf(feeRate);
    }

    public static double getMaxFee(){
        return Double.valueOf(maxFee);
    }

    public static double getMinFee(){
        return Double.valueOf(minFee);
    }

    public static double getMinerFee(Double amount){
        if(amount == null ){
            return 0d;
        }
        if(amount < 0.01){
            return 0.00005D;
        }else if(amount >= 0.01 && amount <1){
            return 0.00008D;
        }else {
            return 0.0001D;
        }
    }

    public static double getTxFee(Double amount){
        if(amount == null){
            return 0D;
        }
        double fee = ArithmeticUtils.multiply(amount,getFeeRate(),8);
        if(fee <= getMinFee()){
            return getMinFee();
        }else if (fee >= getMaxFee()){
            return getMaxFee();
        }else {
            return fee;
        }
    }

    @Value("${bitcoin.service.url}")
    public void setUrl(String url) {
        BitcoinConfig.url = url;
    }

    @Value("${bitcoin.service.port}")
    public void setPort(String port) {
        BitcoinConfig.port = port;
    }

    @Value("${bitcoin.service.username}")
    public void setUsername(String username) {
        BitcoinConfig.username = username;
    }

    @Value("${bitcoin.service.password}")
    public void setPassword(String password) {
        BitcoinConfig.password = password;
    }

    @Value("${bitcoin.fee.rate}")
    public void setFeeRate(String feeRate) {
        BitcoinConfig.feeRate = feeRate;
    }

    @Value("${bitcoin.max.fee}")
    public void setMaxFee(String maxFee) {
        BitcoinConfig.maxFee = maxFee;
    }

    @Value("${bitcoin.min.fee}")
    public void setMinFee(String minFee) {
        BitcoinConfig.minFee = minFee;
    }
}
