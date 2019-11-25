package com.hwq;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by weiqiao.huang on 2019/8/28.
 */
public class Test {

    public static void main(String[] args) {
//        ChannelHandlerContext context = new DefaultChannelHandlerContext();
        //池化的直接内存缓存区
        ByteBuf pooledDirectByteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(1024);
        //池化的堆缓存区
        ByteBuf pooledByteBuf = PooledByteBufAllocator.DEFAULT.buffer(1024);
        //非池化的直接内存缓存区
        ByteBuf unpooledDirectByteBuf = UnpooledByteBufAllocator.DEFAULT.directBuffer(1024);
        //非池化的堆缓存
        ByteBuf unpooledByteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
    }
}
