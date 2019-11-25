package com.hwq.supportpastepackage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by weiqiao.huang on 2019/4/3.
 */
public class TimeClientSupportPastePackageHandler extends ChannelHandlerAdapter{

    private int counter;

    private byte[] req;

    public TimeClientSupportPastePackageHandler(){
        req = ("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf body = null;
        for(int i=0;i<100;i++){
            body = Unpooled.buffer(req.length);
            body.writeBytes(req);
            ctx.writeAndFlush(body);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String)msg;
        System.out.println("Now is :"+body +" ;the counter is :" + ++counter);
    }
}
