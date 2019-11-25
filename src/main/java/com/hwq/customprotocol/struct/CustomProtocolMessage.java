package com.hwq.customprotocol.struct;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by weiqiao.huang on 2019/6/4.
 */
public class CustomProtocolMessage {

    private Header header;

    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
