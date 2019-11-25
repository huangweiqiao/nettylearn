package com.hwq.httpxml.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * Created by weiqiao.huang on 2019/4/26.
 */
public class HttpXmlRequestDecoder extends AbstractHttpXmlDecoder<FullHttpRequest>{
    public HttpXmlRequestDecoder (Class<?> clazz){
        super(clazz,false);
    }
    public HttpXmlRequestDecoder (Class<?> clazz,boolean isPrint){
        super(clazz,isPrint);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, FullHttpRequest request, List<Object> list) throws Exception {
        if(!request.getDecoderResult().isSuccess()){
            sendError( channelHandlerContext, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        HttpXmlRequest httpXmlRequest = new HttpXmlRequest(request,decode0(channelHandlerContext,request.content()));
        list.add(httpXmlRequest);
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status){
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Unpooled.copiedBuffer("Failure: "+status.toString()+"\r\n", CharsetUtil.UTF_8)
        );
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE,"text/plain;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
