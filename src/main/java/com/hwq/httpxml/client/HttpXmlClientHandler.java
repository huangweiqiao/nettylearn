package com.hwq.httpxml.client;

import com.hwq.httpxml.codec.HttpXmlRequest;
import com.hwq.httpxml.codec.HttpXmlResponse;
import com.hwq.httpxml.pojo.OrderFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by weiqiao.huang on 2019/5/9.
 */
public class HttpXmlClientHandler extends SimpleChannelInboundHandler<HttpXmlResponse>{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        HttpXmlRequest request = new HttpXmlRequest(null, OrderFactory.create(1234));
        ctx.writeAndFlush(request);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, HttpXmlResponse httpXmlResponse) throws Exception {
        System.out.println("The client receive response of http header is:"+httpXmlResponse.getHttpResponse().headers().names());
        System.out.println("The client receive response of http body is:"+httpXmlResponse.getResult());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
