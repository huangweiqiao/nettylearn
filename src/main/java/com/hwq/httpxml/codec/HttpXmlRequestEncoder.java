package com.hwq.httpxml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by weiqiao.huang on 2019/4/23.
 */
public class HttpXmlRequestEncoder extends AbstractHttpXmlEncoder<HttpXmlRequest>  {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          HttpXmlRequest httpXmlRequest, List<Object> list) throws Exception {
        ByteBuf body = encode0(channelHandlerContext,httpXmlRequest.getBody());
        FullHttpRequest request = httpXmlRequest.getRequest();
        if (request ==null){
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/do", body);
            HttpHeaders headers = request.headers();
            headers.set(HttpHeaders.Names.HOST, InetAddress.getLocalHost().getHostAddress());
            headers.set(HttpHeaders.Names.CONNECTION,HttpHeaders.Values.CLOSE);
            headers.set(HttpHeaders.Names.ACCEPT_ENCODING,HttpHeaders.Values.GZIP.toString()
                    +","+HttpHeaders.Values.DEFLATE.toString());
            headers.set(HttpHeaders.Names.ACCEPT_CHARSET,"ISO-8859-1,utf-8;q=0.7,*;q=0.7");
            headers.set(HttpHeaders.Names.ACCEPT_LANGUAGE,"zh");
            headers.set(HttpHeaders.Names.USER_AGENT,"Netty xml Http Client side");
            headers.set(HttpHeaders.Names.ACCEPT,"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        }
        //由于请求消息体不为空，也没有使用chunk方式，所以在http消息头中设置消息体的长度Content-Length
        //完成消息体的XML序列化后将重新构造的Http请求消息加入到list中
        HttpHeaders.setContentLength(request,body.readableBytes());
        list.add(request);
    }
}
