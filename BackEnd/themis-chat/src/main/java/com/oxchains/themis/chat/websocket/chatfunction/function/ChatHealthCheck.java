package com.oxchains.themis.chat.websocket.chatfunction.function;

import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.websocket.ChannelHandler;
import com.oxchains.themis.chat.websocket.ChatUtil;
import com.oxchains.themis.chat.websocket.chatfunction.InfoStrategy;
import com.oxchains.themis.common.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;

/**
 * Created by xuqi on 2017/11/7.
 * @author huohuo
 */
public class ChatHealthCheck implements InfoStrategy{
    @Override
    public void disposeInfo(ChatContent chatContent, ChannelHandlerContext ctx) {
        Map<String,ChannelHandler> channelHandlerMap = ChatUtil.userChannels.get(chatContent.getSenderId().toString());
        if(channelHandlerMap != null){
            String keyIDs = ChatUtil.getIDS(chatContent.getSenderId().toString(),chatContent.getReceiverId().toString());
            ChannelHandler channelHandler = channelHandlerMap.get(keyIDs);
            if(channelHandler!=null){
                channelHandler.setLastUseTime(System.currentTimeMillis());
                chatContent.setStatus("success");
                channelHandler.getChannel().writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(chatContent)));
            }
            else{
                chatContent.setStatus("error");
                ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(chatContent)));
            }
        }
    }
}
