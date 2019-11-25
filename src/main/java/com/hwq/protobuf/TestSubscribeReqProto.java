package com.hwq.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiqiao.huang on 2019/4/8.
 */
public class TestSubscribeReqProto {

    private static byte[] encode(SubscribeReqProto.SubscribeReq req){
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte [] body) throws InvalidProtocolBufferException {
        return  SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq(){
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubSeqID(1)
                .setUserName("Lilinfeng")
                .setProductName("yanhua");
        List<String> address = new ArrayList<String>();
        address.add("NanJing YuHuaTai");
        address.add("BeiJing LiuLiChang");
        address.add("ShenZhen HongShuLin");
        builder.addAllAddress(address);
        return builder.build();
    }

    public static void main(String [] args){
        try {
            SubscribeReqProto.SubscribeReq req = createSubscribeReq();
            System.out.println("Before encode :" +req.toString());
            SubscribeReqProto.SubscribeReq req2 =  decode(encode(req));
            System.out.println("after decode :" +req2.toString());
            System.out.println("assert equal :" +req.equals(req2));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
