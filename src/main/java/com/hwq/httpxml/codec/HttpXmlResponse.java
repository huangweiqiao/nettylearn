package com.hwq.httpxml.codec;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * Created by weiqiao.huang on 2019/5/6.
 */
public class HttpXmlResponse {
    private FullHttpResponse httpResponse;
    private Object result;

    public HttpXmlResponse(FullHttpResponse httpResponse,Object result){
        this.httpResponse = httpResponse;
        this.result = result;
    }

    public FullHttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(FullHttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        // TODO
        return "HttpXmlResponse{" +
                "httpResponse=" + httpResponse +
                ", result=" + result +
                '}';
    }
}
