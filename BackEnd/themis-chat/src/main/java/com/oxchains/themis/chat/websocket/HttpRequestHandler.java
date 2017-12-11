package com.oxchains.themis.chat.websocket;
import com.oxchains.themis.chat.entity.SocketPojo;
import com.oxchains.themis.chat.entity.SocketType;
import com.oxchains.themis.chat.websocket.socketFunction.SocketContext;
import com.oxchains.themis.chat.websocket.socketFunction.function.ChatCheck;
import com.oxchains.themis.chat.websocket.socketFunction.function.TXCheck;
import com.oxchains.themis.common.util.JsonUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * create by huohuo
 * @author huohuo
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
        String requestUri =  httpRequest.getUri().toString();
        if (requestUri.contains(wsUri)){
            //连接参数
            String message = requestUri.substring(requestUri.lastIndexOf("?")+1);
            SocketPojo socketPojo = JsonUtil.jsonToEntity(message, SocketPojo.class);
            SocketContext socketContext = null;
            if(socketPojo.getSocketType() == SocketType.CHAT.intValue()){
                socketContext = new SocketContext(new ChatCheck());
                socketContext.disposeInfo(socketPojo,ctx);
            }
            if(socketPojo.getSocketType() == SocketType.TX.intValue()){
                socketContext = new SocketContext(new TXCheck());
                socketContext.disposeInfo(socketPojo,ctx);
            }
            ctx.fireChannelRead(httpRequest.retain());

        }
        else {
            HttpResponse response = new DefaultHttpResponse(httpRequest.getProtocolVersion(), HttpResponseStatus.OK);
                boolean keepAlive = HttpHeaders.isKeepAlive(httpRequest);
            if (keepAlive) {
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            ctx.write(response);
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    private void getIdAndReceiverId(String id,String receiverid,String requestUri){


    }
}
