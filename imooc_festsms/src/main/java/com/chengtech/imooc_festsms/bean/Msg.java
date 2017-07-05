package com.chengtech.imooc_festsms.bean;

/**
 * Created by yingwang on 2015/12/27.
 */
public class Msg {

    public String id;
    public String fesId;
    public String content ;

    public Msg(String id, String fesId, String content) {
        this.id = id;
        this.fesId = fesId;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFesId() {
        return fesId;
    }

    public void setFesId(String fesId) {
        this.fesId = fesId;
    }
}
