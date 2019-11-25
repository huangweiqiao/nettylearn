package com.hwq.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Created by weiqiao.huang on 2019/4/5.
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf>{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        //      获取要解码的byte数组
        final byte[] bytes;
        final int length = msg.readableBytes();
        bytes = new byte[length];
        msg.getBytes(msg.readerIndex(),bytes,0,length);
//      调用MessagePack 的read方法将其反序列化为Object对象
        MessagePack msgPack = new MessagePack();
        out.add(msgPack.read(bytes));
    }
}
