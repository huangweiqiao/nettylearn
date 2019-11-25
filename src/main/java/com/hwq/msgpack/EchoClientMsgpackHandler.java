package com.hwq.msgpack;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by weiqiao.huang on 2019/4/5.
 */
public class EchoClientMsgpackHandler extends ChannelInboundHandlerAdapter {

    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       /* User user = getUser();
        ctx.writeAndFlush(user);*/
        UserInfo[] users = users();
        for (UserInfo u : users) {
            System.out.println(u);
            ctx.writeAndFlush(u);
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("这是客户端接收的消息【  " + ++count + "  】时间:【" + msg + "】");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    private UserInfo[] users(){
        UserInfo [] users = new UserInfo[10];
        for(int i=0;i<10;i++){
            UserInfo userInfo = new UserInfo();
            userInfo.setAge(i);
            userInfo.setName("hwq"+i);
            users[i]=userInfo;
        }
        return users;
    }
}
