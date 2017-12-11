package com.oxchains.themis.chat.websocket.socketFunction;

import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.entity.SocketPojo;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by xuqi on 2017/12/7.
 */
public class SocketContext {
    private SocketStrategy socketStrategy;
    public SocketContext(SocketStrategy socketStrategy){
        this.socketStrategy = socketStrategy;
    }
    public void disposeInfo(SocketPojo socketPojo, ChannelHandlerContext ctx){
        socketStrategy.disposeInfo(socketPojo,ctx);
    }
}
