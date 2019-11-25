package com.hwq.httpxml.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

import java.util.List;

/**
 * Created by weiqiao.huang on 2019/5/7.
 */
public class HttpXmlResponseDecoder extends AbstractHttpXmlDecoder<DefaultFullHttpResponse>{

    public HttpXmlResponseDecoder(Class<?> clazz){
        this(clazz,false);
    }
    public HttpXmlResponseDecoder(Class<?> clazz,boolean isPrintlog){
        super(clazz,false);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DefaultFullHttpResponse defaultFullHttpResponse, List<Object> list) throws Exception {
        Object body = decode0(channelHandlerContext,defaultFullHttpResponse.content());
        HttpXmlResponse response =  new HttpXmlResponse(defaultFullHttpResponse,body);
        list.add(response);
    }
}
