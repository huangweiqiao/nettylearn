package com.hwq.customprotocol.server;

import com.hwq.customprotocol.CustomProtocolConstant;
import com.hwq.customprotocol.codec.CustomProtocolMessageDecoder;
import com.hwq.customprotocol.codec.CustomProtocolMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * Created by weiqiao.huang on 2019/6/11.
 */
public class NettyServer {
    public void bind()throws Exception{
        //配置服务端的NIO线程组
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(boosGroup,workGroup)
               .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new CustomProtocolMessageDecoder(1024*1024,4,4));
                        ch.pipeline().addLast(new CustomProtocolMessageEncoder());
                        ch.pipeline().addLast("readTimeoutHander",new ReadTimeoutHandler(50));
                        ch.pipeline().addLast(new LoginAuthRespHandler());
                        ch.pipeline().addLast("HeartBeatHandler",new HeartBeatRespHandler());
                    }
                });
        // 绑定端口，同步等待成功
        b.bind(CustomProtocolConstant.REMOTEIP,CustomProtocolConstant.PORT).sync();
        System.out.println("Netty server start ok : "
                + (CustomProtocolConstant.REMOTEIP + " : " + CustomProtocolConstant.PORT));
    }

    public static void main(String[] args) {
        try {
            new NettyServer().bind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
