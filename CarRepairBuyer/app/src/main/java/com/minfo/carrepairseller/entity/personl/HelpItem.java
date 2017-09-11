package com.minfo.carrepairseller.entity.personl;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/6/5.
 */

public class HelpItem implements Serializable {
    private String id;
    private String name;
    private String info;
    private String url;
    private String num;

    public String getUrl() {
        return url;
    }

    public String getNum() {
        return num;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
