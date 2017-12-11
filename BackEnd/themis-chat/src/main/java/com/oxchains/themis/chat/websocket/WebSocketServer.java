package com.oxchains.themis.chat.websocket;
import com.oxchains.themis.chat.service.MessageService;
import com.oxchains.themis.common.util.CustomThreadFactory;
import com.oxchains.themis.chat.service.KafkaService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.Executors.newScheduledThreadPool;
/**
 * create by huohuo
 * @author huohuo
 */
@Component
public class WebSocketServer implements Runnable{
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private KafkaService kafkaService;
    private MessageService messageService;
    private Integer port;
    public WebSocketServer(KafkaService kafkaService,Integer port, MessageService messageService){
        this.kafkaService = kafkaService;
        this.port = port;
        this.messageService = messageService;
    }
    public WebSocketServer(KafkaService kafkaService){
        this.kafkaService = kafkaService;
    }

    public WebSocketServer() {}
    protected ScheduledExecutorService keepAliveScheduler = null;
    /*
    * running websocket
    * */

    @Override
    public void run(){
        this.keepAliveScheduler =newScheduledThreadPool(5,new CustomThreadFactory("keep-alive-channel",true));
        this.keepAliveScheduler.schedule(new KeepAliveChannelThread(this.keepAliveScheduler,20),5, TimeUnit.SECONDS);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebsocketChatServerInitializer(kafkaService,messageService))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        }catch (Exception e){
            LOG.error("websocket start faild : {}",e);
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
