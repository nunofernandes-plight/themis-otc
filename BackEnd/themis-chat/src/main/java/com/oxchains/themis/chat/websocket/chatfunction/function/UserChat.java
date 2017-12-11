package com.oxchains.themis.chat.websocket.chatfunction.function;

import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.service.KafkaService;
import com.oxchains.themis.chat.service.MessageService;
import com.oxchains.themis.chat.websocket.ChannelHandler;
import com.oxchains.themis.chat.websocket.ChatUtil;
import com.oxchains.themis.chat.websocket.chatfunction.InfoStrategy;
import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.common.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by xuqi on 2017/11/7.
 * @author huohuo
 */
@Service
public class UserChat implements InfoStrategy{
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private KafkaService kafkaService;
    private ChannelHandlerContext ctx;
    private MessageService messageService;
    public UserChat(KafkaService kafkaService, ChannelHandlerContext ctx, MessageService messageService){
        this.kafkaService = kafkaService;
        this.ctx = ctx;
        this.messageService = messageService;
    }

    public UserChat() {
    }
    @Override
    public void disposeInfo(ChatContent chatContent,ChannelHandlerContext ctx) {
        try {
            //接收到消息后  先给自己转发一份 在给对方转法一份 如果对方不在线则 发到私信里面 然后将消息存到kafka队列里 由kafka存到mongo里
            String keyIDs = ChatUtil.getIDS(chatContent.getSenderId().toString(),chatContent.getReceiverId().toString());
            chatContent.setCreateTime(DateUtil.getPresentDate());
            chatContent.setChatId(keyIDs);
            String message = JsonUtil.toJson(chatContent).toString();
            ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
            //再给对方转发
            Map<String,ChannelHandler> channelHandlerMap = ChatUtil.userChannels.get(chatContent.getReceiverId().toString());
            if( channelHandlerMap!= null && channelHandlerMap.get(keyIDs)!=null){
                channelHandlerMap.get(keyIDs).getChannel().writeAndFlush(new TextWebSocketFrame(message));
            }
            else{
                messageService.postPriChatMessage(chatContent);
            }
            kafkaService.send(message);
        } catch (Exception e) {
            LOG.error("caht disposeInfo faild : {}",e);
        }
    }
}
