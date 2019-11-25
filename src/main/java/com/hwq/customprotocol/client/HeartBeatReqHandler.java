package com.hwq.customprotocol.client;

import com.hwq.customprotocol.MessageType;
import com.hwq.customprotocol.struct.CustomProtocolMessage;
import com.hwq.customprotocol.struct.Header;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/**
 * Created by weiqiao.huang on 2019/6/10.
 */
public class HeartBeatReqHandler extends ChannelHandlerAdapter {

    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CustomProtocolMessage message = (CustomProtocolMessage)msg;
        if(message.getHeader() !=null
                && message.getHeader().getType() == MessageType.LOGIN_RESP.value()){
            heartBeat = ctx.executor().scheduleAtFixedRate(
                    new HeartBeatReqHandler().new HeartBeatTask(ctx),0,5000, TimeUnit.MILLISECONDS);
        }else if(message.getHeader()!=null && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()){
            System.out.println("Client receive server heart beat message: ---> "+message);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if(heartBeat !=null ){
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }

    private class HeartBeatTask implements Runnable{
        private final ChannelHandlerContext ctx;
        public HeartBeatTask(final ChannelHandlerContext ctx){
            this.ctx = ctx;
        }

        @Override
        public void run() {
            CustomProtocolMessage heatBeat = buildHeatBeat();
            System.out.println("Client send heart beat message to Server : ---> "+heatBeat);
            ctx.writeAndFlush(heatBeat);
        }

        private CustomProtocolMessage  buildHeatBeat(){
            CustomProtocolMessage message = new CustomProtocolMessage();
            Header header = new Header();
            header.setLength(123);
            header.setSessionID(99999);
            header.setPriority((byte) 7);
            header.setType(MessageType.HEARTBEAT_REQ.value());
            message.setHeader(header);
            return message;
        }

    }
}
