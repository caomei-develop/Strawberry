package com.cm.strawberry.bean;

/**
 * Created by zhouwei on 17-7-28.
 */

public class WxAiccle {
    public String cid;
    public String name;

    public WxAiccle(String cid, String name) {
        this.cid = cid;
        this.name = name;

    }

    public String getCid() {

        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
