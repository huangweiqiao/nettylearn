package com.hwq.httpxml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;

import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.List;


/**
 * Created by weiqiao.huang on 2019/4/24.
 */
public abstract   class AbstractHttpXmlEncoder<I> extends MessageToMessageEncoder<I>{

    IBindingFactory factory=null;
    StringWriter writer = null;
    final static String CHARSET_NAME="UTF-8";
    final static Charset UTF_8 = Charset.forName(CHARSET_NAME);

    protected ByteBuf encode0(ChannelHandlerContext ctx,Object obj) throws Exception {
        factory = BindingDirectory.getFactory(obj.getClass());
        IMarshallingContext mctx = factory.createMarshallingContext();
        writer = new StringWriter();
        mctx.setIndent(2);
        mctx.marshalDocument(obj,CHARSET_NAME,null,writer);
        String xmlStr = writer.toString();
        writer.close();
        writer=null;
        ByteBuf encodeBuf = Unpooled.copiedBuffer(xmlStr, UTF_8);
        return encodeBuf;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (writer!=null){
            writer.close();
            writer = null;
        }
    }
}
