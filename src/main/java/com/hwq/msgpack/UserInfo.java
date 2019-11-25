package com.hwq.msgpack;

import org.msgpack.annotation.Message;

/**
 * Created by weiqiao.huang on 2019/4/5.
 */
@Message
public class UserInfo {
    private int age;
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return this.name+"--->"+this.age;
    }
}
