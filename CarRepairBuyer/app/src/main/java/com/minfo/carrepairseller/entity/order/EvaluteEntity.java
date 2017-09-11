package com.minfo.carrepairseller.entity.order;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/6/21.
 */
public class EvaluteEntity implements Serializable {
    private String content;
    private String name;
    private String picture;
    private String addtime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "EvaluteEntity{" +
                "content='" + content + '\'' +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", addtime='" + addtime + '\'' +
                '}';
    }
}
