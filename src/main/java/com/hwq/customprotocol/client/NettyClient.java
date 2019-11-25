package com.hwq.customprotocol.client;

import com.hwq.customprotocol.CustomProtocolConstant;
import com.hwq.customprotocol.codec.CustomProtocolMessageDecoder;
import com.hwq.customprotocol.codec.CustomProtocolMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by weiqiao.huang on 2019/6/10.
 */
public class NettyClient {

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(int port,String host) throws Exception{
        // 配置客户端NIO线程组
        try{
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new CustomProtocolMessageDecoder(1024*1024,4,4));
                            ch.pipeline().addLast("customProtocolMessageEncoder",new CustomProtocolMessageEncoder());
                            ch.pipeline().addLast("readTimeoutHandler",new ReadTimeoutHandler(50));
                            ch.pipeline().addLast("LoginAuthHandler",new LoginAuthReqHandler());
                            ch.pipeline().addLast("HeartBeatHandler",new HeartBeatReqHandler());
                        }
                    });
            //发起异步连接
            ChannelFuture future = b.connect(
                    new InetSocketAddress(host,port),
                    new InetSocketAddress(CustomProtocolConstant.LOCALIP,CustomProtocolConstant.LOCAL_PORT)
            ).sync();
            //当对应的channel关闭的时候就会返回对应的channel
            future.channel().closeFuture().sync();
        }finally {
            //多有资源释放完成之后，清空资源，再次发起重连操作
            executor.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(1);
                    try {
                        connect(CustomProtocolConstant.PORT,CustomProtocolConstant.REMOTEIP);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void main(String [] args)throws Exception{
        new NettyClient().connect(CustomProtocolConstant.PORT,CustomProtocolConstant.REMOTEIP);
    }

}
