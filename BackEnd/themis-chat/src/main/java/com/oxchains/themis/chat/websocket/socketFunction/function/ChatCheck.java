package com.oxchains.themis.chat.websocket.socketFunction.function;

import com.oxchains.themis.chat.entity.SocketPojo;
import com.oxchains.themis.chat.websocket.ChannelHandler;
import com.oxchains.themis.chat.websocket.ChatUtil;
import com.oxchains.themis.chat.websocket.socketFunction.SocketStrategy;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xuqi on 2017/12/7.
 */
public class ChatCheck implements SocketStrategy {
    @Override
    public void disposeInfo(SocketPojo socketPojo, ChannelHandlerContext ctx) {
        if(socketPojo.getUserId()!=null && socketPojo.getPartnerId() !=null){
            //判断当前用户的channel分区是否已经创建，如未创建 则创建之
            String id = socketPojo.getUserId().toString();
            String receiverId = socketPojo.getPartnerId().toString();
            if(ChatUtil.userChannels.get(id) == null){
                ChatUtil.userChannels.put(id,new ConcurrentHashMap<String ,ChannelHandler>());
            }
            Map<String,ChannelHandler> channelHandlerMap =  ChatUtil.userChannels.get(id);
            String keyIds = ChatUtil.getIDS(id,receiverId);
            //如果连接存在 则把以前的连接关闭掉 建立新的连接
            if(channelHandlerMap.get(keyIds) != null){
                channelHandlerMap.get(keyIds).close();
                channelHandlerMap.remove(keyIds);
            }
            channelHandlerMap.put(keyIds,new ChannelHandler(ctx.channel(),System.currentTimeMillis()));
        }
    }
}
