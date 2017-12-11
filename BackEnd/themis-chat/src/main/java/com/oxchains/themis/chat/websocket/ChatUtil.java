package com.oxchains.themis.chat.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * create by huohuo
 * @author huohuo
 */
public class ChatUtil {

    public static Map<String,Map<String,ChannelHandler>> userChannels = new ConcurrentHashMap<>();
    public static Map<String,ChannelHandler> txChannels = new ConcurrentHashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(ChatUtil.class);
    public  static String getIDS(String id,String did){
        return Integer.parseInt(id) > Integer.parseInt(did)? did+"_"+id : id+"_"+did;
    }

}
