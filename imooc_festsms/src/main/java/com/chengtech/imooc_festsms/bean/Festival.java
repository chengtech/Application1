package com.chengtech.imooc_festsms.bean;

/**
 * Created by Administrator on 2015/12/16.
 */
public class Festival {

    private String id;
    private String content;

    public Festival() {
    }

    public Festival(String id,String content) {
        this.id = id;
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
}
