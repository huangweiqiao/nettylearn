package com.hwq.httpxml.server;

import com.hwq.httpxml.codec.HttpXmlRequest;
import com.hwq.httpxml.codec.HttpXmlResponse;
import com.hwq.httpxml.pojo.Address;
import com.hwq.httpxml.pojo.Order;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiqiao.huang on 2019/5/8.
 */
public class HttpXmlServerHandler extends SimpleChannelInboundHandler<HttpXmlRequest>{
    @Override
    protected void messageReceived(final ChannelHandlerContext channelHandlerContext, HttpXmlRequest httpXmlRequest) throws Exception {
        HttpRequest request = httpXmlRequest.getRequest();
        Order order = (Order) httpXmlRequest.getBody();
        System.out.println("Http server receive reqeust:" + order);
        dobusiness(order);
        ChannelFuture future = channelHandlerContext.writeAndFlush(new HttpXmlResponse(null,order));
        if(!HttpHeaders.isKeepAlive(request)){
            future.addListener(new GenericFutureListener<Future<? super Void>>(){
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    channelHandlerContext.close();
                }
            });
        }
    }

    private void dobusiness(Order order) {
        order.getCustomer().setFirstName("狄");
        order.getCustomer().setLastName("仁杰");
        List<String> midNames = new ArrayList<String>();
        midNames.add("李元芳");
        order.getCustomer().setMiddleNames(midNames);
        Address address = order.getBillTo();
        address.setCity("洛阳");
        address.setCountry("大唐");
        address.setState("河南道");
        address.setPostCode("123456");
        order.setBillTo(address);
        order.setShipTo(address);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(ctx.channel().isActive()){
            sendError( ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status){
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,status,
                Unpooled.copiedBuffer("失败："+status.toString()+"\r\n", CharsetUtil.UTF_8));
    }
}
