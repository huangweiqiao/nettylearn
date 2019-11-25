package com.hwq.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by weiqiao.huang on 2019/4/9.
 */
public class HttpFileServer {

    private static final String DEFAULT_URL = "/src/main/java/com/hwq/";

    public void run(final int port ,final String url){
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootStrap = new ServerBootstrap();
            bootStrap.group(boosGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                            //Http解码器在每个http消息中会生成多个消息对象，HttpRequest/HttpResponse,HttpContent,LastHttpContent,
                            // 现在加上HttpObjectAggregator就可以将多个对象合成一个FullHttpRequest/FullHttpResponse对象
                            ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                            // ChunkedWriteHandler支持异步发送大的码流，可以防止内存溢出
                            ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                            ch.pipeline().addLast("fileServerHandler",new HttpFileServerHandler(url));
                        }
                    });
            ChannelFuture future = bootStrap.bind("127.0.0.1",port).sync();
            System.out.println("Http 文件目录服务器启动，通过浏览器访问网址：http://127.0.0.1:"+port+url);
            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new HttpFileServer().run(12345,DEFAULT_URL);
    }
}
