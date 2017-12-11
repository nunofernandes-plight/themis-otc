package com.oxchains.themis.chat.websocket.chatfunction.function;

import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.websocket.ChannelHandler;
import com.oxchains.themis.chat.websocket.ChatUtil;
import com.oxchains.themis.chat.websocket.chatfunction.ChatContext;
import com.oxchains.themis.chat.websocket.chatfunction.InfoStrategy;
import com.oxchains.themis.common.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * Created by xuqi on 2017/12/8.
 */
public class UploadTxHealthCheck implements InfoStrategy {
    @Override
    public void disposeInfo(ChatContent chatContent, ChannelHandlerContext ctx) {
        ChannelHandler channelHandler = ChatUtil.txChannels.get(chatContent.getOrderId());
        ChatContent chatContent1 = new ChatContent();
        if(channelHandler != null){
            channelHandler.setLastUseTime(System.currentTimeMillis());
            chatContent1.setStatus("success");
            channelHandler.getChannel().writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(chatContent1)));
        }
        else{
            chatContent1.setStatus("error");
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(chatContent1)));
        }
    }
}
