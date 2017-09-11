package com.minfo.carrepair.entity.personl;

import java.io.Serializable;

/**
 * Created by liujing on 15/9/7.
 */
public class User implements Serializable {
    private String name;
    private String img;

    private String version;
    private String down_url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDown_url() {
        return down_url;
    }

    public void setDown_url(String down_url) {
        this.down_url = down_url;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", version='" + version + '\'' +
                ", down_url='" + down_url + '\'' +
                '}';
    }
}
