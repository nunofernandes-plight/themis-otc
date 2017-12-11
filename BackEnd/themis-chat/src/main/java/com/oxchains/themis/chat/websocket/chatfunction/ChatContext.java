package com.oxchains.themis.chat.websocket.chatfunction;

import com.oxchains.themis.chat.entity.ChatContent;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by xuqi on 2017/11/6.
 * @author huohuo
 */
public class ChatContext {
    private InfoStrategy infoStrategy;
    public ChatContext(InfoStrategy infoStrategy){
        this.infoStrategy = infoStrategy;
    }
    public void disposeInfo(ChatContent chatContext, ChannelHandlerContext ctx){
        infoStrategy.disposeInfo(chatContext,ctx);
    }
}
