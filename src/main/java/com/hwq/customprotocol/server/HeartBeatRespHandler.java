package com.hwq.customprotocol.server;

import com.hwq.customprotocol.MessageType;
import com.hwq.customprotocol.struct.CustomProtocolMessage;
import com.hwq.customprotocol.struct.Header;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by weiqiao.huang on 2019/6/11.
 */
public class HeartBeatRespHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CustomProtocolMessage message = (CustomProtocolMessage)msg;
        //返回心跳应答消息
        if(message.getHeader()!=null
                && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()){
            System.out.println("Receive client heart beat message : ---> "
                    + message);
            CustomProtocolMessage heartBeat = buildHeatBeat();
            System.out.println("Send heart beat response message to client : ---> "
                    + heartBeat);
            ctx.writeAndFlush(heartBeat);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    private CustomProtocolMessage buildHeatBeat(){
        CustomProtocolMessage message = new CustomProtocolMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }
}
