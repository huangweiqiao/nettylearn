package com.hwq.httpxml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.List;

/**
 * Created by weiqiao.huang on 2019/5/6.
 */
public class HttpXmlResponseEncoder extends AbstractHttpXmlEncoder<HttpXmlResponse>{

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, HttpXmlResponse httpXmlResponse, List<Object> list) throws Exception {

        ByteBuf body = encode0(channelHandlerContext,httpXmlResponse.getResult());
        FullHttpResponse response = httpXmlResponse.getHttpResponse();
        if(response ==null){
          response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,body);
        }else{
            response = new DefaultFullHttpResponse(httpXmlResponse.getHttpResponse().getProtocolVersion(),
                    httpXmlResponse.getHttpResponse().getStatus(),body);
        }
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE,"text/xml");
        HttpHeaders.setContentLength(response,body.readableBytes());
        list.add(response);
    }
}
