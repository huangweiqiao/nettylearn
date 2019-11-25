package com.hwq.customprotocol.client;

import com.hwq.customprotocol.MessageType;
import com.hwq.customprotocol.struct.CustomProtocolMessage;
import com.hwq.customprotocol.struct.Header;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by weiqiao.huang on 2019/6/10.
 */
public class LoginAuthReqHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CustomProtocolMessage message = (CustomProtocolMessage)msg;
        //如果是握手应答消息，需要判断是否认证成功
        if(message.getHeader() !=null
                && message.getHeader().getType() == MessageType.LOGIN_RESP.value() ){
            byte loginResult = (byte) message.getBody();
            if(loginResult != (byte)0){
                //握手失败 关闭连接
                ctx.close();
            }else{
                System.out.println("Login is ok :"+message);
                ctx.fireChannelRead(msg);
            }
        }else{
            ctx.fireChannelRead(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

    private CustomProtocolMessage buildLoginReq(){
        CustomProtocolMessage message = new CustomProtocolMessage();
        Header header = new Header();
        header.setLength(123);
        header.setSessionID(99999);
        header.setPriority((byte) 7);
        header.setType(MessageType.LOGIN_REQ.value());
        message.setHeader(header);
        return message;
    }
}
