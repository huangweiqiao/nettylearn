package com.hwq.unsupportpastepackage;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * Created by weiqiao.huang on 2019/4/1.
 */
public class ChildChannelHandler extends ChannelInitializer{
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new TimeServerHandler());
    }

    public static void main(String [] args) {
        try {
            new TimeServer().bind(12345);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
