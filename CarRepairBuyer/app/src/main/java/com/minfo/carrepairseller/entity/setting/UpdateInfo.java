package com.minfo.carrepairseller.entity.setting;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/4/6.
 */

public class UpdateInfo implements Serializable{
    private String url;
    private int num;
    private String versionName;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

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
