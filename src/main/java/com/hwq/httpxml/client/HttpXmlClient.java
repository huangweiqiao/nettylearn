package com.hwq.httpxml.client;

import com.hwq.httpxml.codec.HttpXmlRequestEncoder;
import com.hwq.httpxml.codec.HttpXmlResponseDecoder;
import com.hwq.httpxml.pojo.Order;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

import java.net.InetSocketAddress;

/**
 * Created by weiqiao.huang on 2019/5/9.
 */
public class HttpXmlClient {

    public void connect(int port){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("http-decoder",new HttpResponseDecoder());
                            socketChannel.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
                            socketChannel.pipeline().addLast("xml-decoder",new HttpXmlResponseDecoder(Order.class,true));
                            socketChannel.pipeline().addLast("http-encoder",new HttpRequestEncoder());
                            socketChannel.pipeline().addLast("xml-encoder",new HttpXmlRequestEncoder());
                            socketChannel.pipeline().addLast("xmlClientHandler",new HttpXmlClientHandler());
                        }
                    });
            ChannelFuture future = b.connect(new InetSocketAddress(port)).sync();
            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new HttpXmlClient().connect(12345);
    }

}
