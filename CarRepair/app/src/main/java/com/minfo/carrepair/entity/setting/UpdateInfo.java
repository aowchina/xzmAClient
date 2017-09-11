package com.minfo.carrepair.entity.setting;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/4/6.
 */

public class UpdateInfo implements Serializable{
    private String url;
    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
