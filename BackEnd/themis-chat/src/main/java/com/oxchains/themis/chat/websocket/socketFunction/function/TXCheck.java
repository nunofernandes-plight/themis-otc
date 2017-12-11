package com.oxchains.themis.chat.websocket.socketFunction.function;

import com.oxchains.themis.chat.entity.SocketPojo;
import com.oxchains.themis.chat.websocket.ChannelHandler;
import com.oxchains.themis.chat.websocket.ChatUtil;
import com.oxchains.themis.chat.websocket.socketFunction.SocketStrategy;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

/**
 * Created by xuqi on 2017/12/7.
 */
public class TXCheck implements SocketStrategy {
    @Override
    public void disposeInfo(SocketPojo socketPojo, ChannelHandlerContext ctx) {
        Map<String, ChannelHandler> txChannels = ChatUtil.txChannels;
        if(txChannels.get(socketPojo.getOrderId()) != null){
            txChannels.get(socketPojo.getOrderId()).close();
            txChannels.remove(socketPojo.getOrderId());
        }
        txChannels.put(socketPojo.getOrderId(),new ChannelHandler(ctx.channel(),System.currentTimeMillis()));

    }
}
