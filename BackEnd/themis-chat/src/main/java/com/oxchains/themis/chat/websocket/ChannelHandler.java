package com.oxchains.themis.chat.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
/**
 * create by huohuo
 * @author huohuo
 */
public class ChannelHandler {
    private Channel channel;
    private long lastUseTime;
    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public long getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(long lastUseTime) {
        this.lastUseTime = lastUseTime;
    }

    public ChannelHandler(Channel channel, long lastUseTime) {
        this.channel = channel;
        this.lastUseTime = lastUseTime;
    }
    public void close(){
        if(this.channel!=null){
            ChannelFuture cf = this.channel.closeFuture();
            cf.channel().close();
        }
    }
}
