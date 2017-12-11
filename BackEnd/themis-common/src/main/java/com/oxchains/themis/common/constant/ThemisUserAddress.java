package com.oxchains.themis.common.constant;

/**
 * Created by xuqi on 2017/11/6.
 * @author huohuo
 */
public class ThemisUserAddress {
    public static final String GET_ADDRESS_KEYS = "http://themis-user/account/keys"; //获取一对随机的公私钥
    public static final String MOVE_BTC = "http://themis-user/account/p2ur";         //订单完成，订单取消，仲裁完成 转移BTC的接口
    public static final String CHECK_BTC = "http://themis-user/account/";             //get卖家确认订单判断BTC有没有到账 post 上传交易id
    public static final String CREATE_CENTET_ADDRESS = "http://themis-user/account/p2sh";//创建协商地址
    public static final String GET_ARBITRATE_USER = "http://themis-user/user/arbitrations";  //获取仲裁者用户
    public static final String GET_USER = "http://themis-user/user/findOne?id=";  //获取仲裁者用户
    public static final String GET_PTSHADDRESS = "http://themis-user/account/transaction/";  //根据订单id获取协商地址
    public static final String SAVE_ARBITRATE = "http://themis-arbitrate/arbitrate/saveOrderAbritrate";  //根据订单id获取协商地址
    public static final String GET_NOTICE = "http://themis-notice/notice/query/noticeId/";  //根据订单id获取协商地址
    public static final String TX_INFORM = "http://themis-chat/chat/uploadTxInform";   //上传tx之后的通知
}
