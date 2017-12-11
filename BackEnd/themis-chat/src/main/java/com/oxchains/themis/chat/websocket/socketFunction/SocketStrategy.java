package com.oxchains.themis.chat.websocket.socketFunction;

import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.entity.SocketPojo;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by xuqi on 2017/12/7.
 */
public interface SocketStrategy {
    public void disposeInfo(SocketPojo socketPojo, ChannelHandlerContext ctx);
}
