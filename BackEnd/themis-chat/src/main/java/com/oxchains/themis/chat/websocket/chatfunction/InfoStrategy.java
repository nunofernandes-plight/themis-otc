package com.oxchains.themis.chat.websocket.chatfunction;

import com.oxchains.themis.chat.entity.ChatContent;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by xuqi on 2017/11/6.
 * @author huohuo
 */
public interface InfoStrategy {
    public void disposeInfo(ChatContent chatContent, ChannelHandlerContext ctx);
}
