package com.hwq.customprotocol.server;

import com.hwq.customprotocol.MessageType;
import com.hwq.customprotocol.struct.CustomProtocolMessage;
import com.hwq.customprotocol.struct.Header;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by weiqiao.huang on 2019/6/11.
 */
public class LoginAuthRespHandler extends ChannelHandlerAdapter {

    private Map<String,Boolean> nodeCheck = new ConcurrentHashMap<String,Boolean>();
    private String [] whiteList = {"127.0.0.1"};
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CustomProtocolMessage message = (CustomProtocolMessage)msg;
        if(message.getHeader()!=null
                && message.getHeader().getType() == MessageType.LOGIN_REQ.value()){
            String nodeIndex = ctx.channel().remoteAddress().toString();
            CustomProtocolMessage loginResp = null;
            //重复登录，拒绝
            if (nodeCheck.containsKey(nodeIndex)){
                loginResp = buildResponse((byte)-1);
            }else {
                 InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
                 String ip = address.getAddress().getHostAddress();
                boolean isOK = false;
                for(String WIP:whiteList){
                    if(WIP.equals(ip)){
                        isOK = true;
                        break;
                    }
                }
                loginResp = isOK ? buildResponse((byte)0): buildResponse((byte)-1);
                if(isOK){
                    nodeCheck.put(nodeIndex,true);
                }
            }
            System.out.println("The login response is : " + loginResp
                    + " body [" + loginResp.getBody() + "]");
            ctx.writeAndFlush(loginResp);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        nodeCheck.remove(ctx.channel().remoteAddress().toString());//删除缓存
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }

    private CustomProtocolMessage buildResponse(byte body){
        CustomProtocolMessage message = new CustomProtocolMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.value());
        message.setHeader(header);
        message.setBody(body);
        return message;
    }
}
