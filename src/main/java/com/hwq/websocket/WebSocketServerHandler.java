package com.hwq.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by weiqiao.huang on 2019/5/13.
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler <Object>{

    private static final Logger logger = Logger.getLogger(WebSocketServerHandler.class.getName());

    private  WebSocketServerHandshaker  handshaker= null;

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        if(msg instanceof FullHttpRequest){
            //传统的http请求
            handleHttpRequest(channelHandlerContext,(FullHttpRequest)msg);
        }else if(msg instanceof WebSocketFrame){
            //websocket请求
            handleWebSocketFrame(channelHandlerContext,(WebSocketFrame)msg);
        }
    }

    private void handleHttpRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest request){
        //如果http解码失败 或 不是websocket请求则返回异常
        System.out.println(request.getDecoderResult().isSuccess());
        System.out.println(request.headers().get("Upgrade"));
        if(!request.getDecoderResult().isSuccess()
                || !"websocket".equalsIgnoreCase(request.headers().get("Upgrade"))){
            sendHttpResponse(channelHandlerContext,request,new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.BAD_REQUEST
            ));
            return ;
        }
        //构造握手响应返回
        WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(
                "ws://localhost:12345",null,false
        );
        handshaker = factory.newHandshaker(request);
        if(handshaker==null){
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(channelHandlerContext.channel());
        }else {
            handshaker.handshake(channelHandlerContext.channel(),request);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext channelHandlerContext,WebSocketFrame webSocketFrame){
        if(webSocketFrame instanceof CloseWebSocketFrame){
            //是关闭指令
            handshaker.close(channelHandlerContext.channel(),(CloseWebSocketFrame) webSocketFrame.retain());
            return;
        }
        if (webSocketFrame instanceof PingWebSocketFrame){
            //ping 消息
            channelHandlerContext.channel().write(new PongWebSocketFrame(webSocketFrame.content().retain()));
        }
        //本例程仅支持文本消息，不支持二进制消息
        if (!(webSocketFrame instanceof TextWebSocketFrame)){
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported",
                    webSocketFrame.getClass().getName()
            ));
        }
        //返回消息
        String reqStr = ((TextWebSocketFrame)webSocketFrame).text();
        if (logger.isLoggable(Level.FINE)){
            logger.fine(String.format("%s received %s",channelHandlerContext.channel(),reqStr));
        }
        channelHandlerContext.channel().write(new TextWebSocketFrame(reqStr+
                ",欢迎使用Netty WebSocket服务，现在时刻:"+
                new Date().toString()));
    }

    private void sendHttpResponse(ChannelHandlerContext channelHandlerContext,FullHttpRequest request,
                                  FullHttpResponse response  ){
        //应答客户端
        if(response.getStatus().code()!=200){
            ByteBuf buf = Unpooled.copiedBuffer(response.getStatus().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
            HttpHeaders.setContentLength(response,response.content().readableBytes());
        }
        //如果是非keep-alive，关闭连接
        ChannelFuture future = channelHandlerContext.channel().writeAndFlush(response);
        if(!HttpHeaders.isKeepAlive(request)
                || response.getStatus().code()!=200){
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
