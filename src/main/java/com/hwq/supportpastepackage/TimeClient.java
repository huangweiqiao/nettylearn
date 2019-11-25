package com.hwq.supportpastepackage;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by weiqiao.huang on 2019/4/3.
 */
public class TimeClient {

    public void connect(int port,String host)throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap boot = new Bootstrap();
        boot.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new TimeClientSupportPastePackageHandler());
                    }
                });
        ChannelFuture future = boot.connect(host,port).sync();
        future.channel().closeFuture().sync();
    }


    public static void main(String[] args) {
        TimeClient client = new TimeClient();
        try {
            client.connect(12345,"127.0.0.1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
