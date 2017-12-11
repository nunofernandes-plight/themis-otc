package com.oxchains.themis.chat.websocket;
import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.entity.ChatParam;
import com.oxchains.themis.chat.service.KafkaService;
import com.oxchains.themis.chat.service.MessageService;
import com.oxchains.themis.chat.websocket.chatfunction.ChatContext;
import com.oxchains.themis.chat.websocket.chatfunction.function.ChatHealthCheck;
import com.oxchains.themis.chat.websocket.chatfunction.function.UploadTxHealthCheck;
import com.oxchains.themis.chat.websocket.chatfunction.function.UserChat;
import com.oxchains.themis.common.util.JsonUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
/**
 * create by huohuo
 * @author huohuo
 */
public class TextWebSocketFrameHandler extends
		SimpleChannelInboundHandler<TextWebSocketFrame> {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	private KafkaService kafkaService;
	private MessageService messageService;
	public TextWebSocketFrameHandler(KafkaService kafkaService,MessageService messageService){
    this.kafkaService = kafkaService;
    this.messageService = messageService;
	}
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
								TextWebSocketFrame msg) throws Exception {
		ChatContent chatContent= (ChatContent) JsonUtil.fromJson(msg.text(), ChatContent.class);
		ChatContext chatContext = null;
		//消息类型是健康心跳
		if(chatContent.getMsgType() == ChatParam.MsgType.HEALTH_CHECK.getStatus().intValue()){
			if(chatContent.getHealthType() == ChatParam.HealthType.CHAT_HEALTH.getStatus().intValue()){
				chatContext = new ChatContext(new ChatHealthCheck());
				chatContext.disposeInfo(chatContent,ctx);
			}
			if(chatContent.getHealthType() == ChatParam.HealthType.UPLOAD_TXID_HEALTH.getStatus().intValue()){
				chatContext = new ChatContext(new UploadTxHealthCheck());
				chatContext.disposeInfo(chatContent,ctx);
			}
		}
		//消息类型是聊天
		if(chatContent.getMsgType() == ChatParam.MsgType.USER_CHAT.getStatus().intValue()){
			chatContext = new ChatContext(new UserChat(kafkaService,ctx,messageService));
			chatContext.disposeInfo(chatContent,ctx);
		}
	}
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
			ctx.pipeline().remove(HttpRequestHandler.class);
			channels.add(ctx.channel());
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		channels.add(incoming);
	}
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		incoming.closeFuture();
		channels.remove(incoming);

	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		Channel incoming = ctx.channel();
		cause.printStackTrace();
		ctx.close();
		channels.remove(incoming);
	}

}
