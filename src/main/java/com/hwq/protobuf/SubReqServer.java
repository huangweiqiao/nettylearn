package com.hwq.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by weiqiao.huang on 2019/4/8.
 */
public class SubReqServer {

    public void bind(int port){
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap boot = new ServerBootstrap();
        try {
            boot.group(boosGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            /**
                             * 处理半包的解码器有三种方式可以选择
                             * 1、使用netty提供的 ProtobufVarint32FrameDecoder
                             * 2、继承netty提供的通用半包解码器 LengthFieldBasedFrameDecoder
                             * 3、继承ByteToMessageDecoder类自己实现半包处理
                             */

                            /**
                             * 添加半包处理的解码器，这个与ProtobufVarint32LengthFieldPrepender半包编码器对应
                             * reader的情况 pipeline中的解码器调用顺序是  first--->last, 编码器对读不起作用
                             */
                            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                            //添加内容解码器，这个只负责解码处理不了半包问题，所以在前面要加半包解码器，参数告诉解码器要将字节信息解密成那个对象
                            ch.pipeline().addLast(new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));
                            /**
                             * 添加半包编码器，因为ProtobufEncoder只是按照规则将消息的各个字段输出，并没有serializedSize，
                             * 所以socket无法判断闭包，因此要在ProtobufEncoder生成的字节数组前 prepender一个varint32数字，表示serializedSize。
                             * write的情况 pipeline中的编码器调用顺序是 last--->first，解码器对写不起作用
                             */
                            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                            ch.pipeline().addLast(new ProtobufEncoder());
                            ch.pipeline().addLast(new SubReqServerHandler());
                        }
                    });
            ChannelFuture future = boot.bind(port).sync();
            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        SubReqServer server = new SubReqServer();
        server.bind(12345);
    }
}
