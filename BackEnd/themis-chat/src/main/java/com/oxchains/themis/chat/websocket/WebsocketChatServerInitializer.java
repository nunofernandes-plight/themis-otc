package com.oxchains.themis.chat.websocket;

import com.oxchains.themis.chat.service.KafkaService;
import com.oxchains.themis.chat.service.MessageService;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * create by huohuo
 * @author huohuo
 */
public class WebsocketChatServerInitializer extends
        ChannelInitializer<SocketChannel> {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	private KafkaService kafkaService;
	private MessageService messageService;
	public WebsocketChatServerInitializer(KafkaService kafkaService,MessageService messageService){
		this.kafkaService = kafkaService;
		this.messageService = messageService;
	}
	@Override
    public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new HttpObjectAggregator(64*1024));
		pipeline.addLast(new ChunkedWriteHandler());
		pipeline.addLast(new HttpRequestHandler("/ws"));
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		pipeline.addLast(new TextWebSocketFrameHandler(kafkaService,messageService));

    }
}
