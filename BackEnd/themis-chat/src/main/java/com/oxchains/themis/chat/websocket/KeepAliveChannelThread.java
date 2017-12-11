package com.oxchains.themis.chat.websocket;

import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * create by huohuo
 * @author huohuo
 */
public class KeepAliveChannelThread implements Runnable {
    private ScheduledExecutorService keepAliveScheduler;
    private long keepTime;
    private static final Logger LOG = LoggerFactory.getLogger(KeepAliveChannelThread.class);
    public KeepAliveChannelThread(ScheduledExecutorService keepAliveScheduler, long keepTime) {
        this.keepAliveScheduler = keepAliveScheduler;
        this.keepTime = keepTime;
    }
    @Override
    public void run() {
        try {
        for (String s : ChatUtil.userChannels.keySet()) {
            for (String s1 : ChatUtil.userChannels.get(s).keySet()){
                if (System.currentTimeMillis() - ChatUtil.userChannels.get(s).get(s1).getLastUseTime() > (3*1000)){
                    ChannelFuture cf =  ChatUtil.userChannels.get(s).get(s1).getChannel().closeFuture();
                        cf.channel().close().sync();
                        ChatUtil.userChannels.get(s).remove(s1);
                        TextWebSocketFrameHandler.channels.remove(cf.channel());
                }
            }
        }
        /*for (String s : ChatUtil.txChannels.keySet()){
            if(System.currentTimeMillis() - ChatUtil.txChannels.get(s).getLastUseTime() > 3*1000){
                ChannelFuture channelFuture = ChatUtil.txChannels.get(s).getChannel().closeFuture();
                channelFuture.channel().close().sync();
                ChatUtil.txChannels.remove(s);
                TextWebSocketFrameHandler.channels.remove(channelFuture.channel());
            }
        }*/
        }catch (Exception e){
            LOG.error("Keep Alive websocket channel faild : {}",e);
        }
        this.keepAliveScheduler.schedule(this,keepTime, TimeUnit.SECONDS);


    }
}
