package com.minfo.carrepair.entity.personl;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/7/12.
 */

public class KefuItem implements Serializable {
//    {
//        "picture": "http://zjxzm.min-fo.com/downLoad/seller/c8/1e/72/8d/2/1500001522.png",
//            "name": "小张",
//            "tel": "18301202197",
//            "sellerid": "3"
//    }
    @SerializedName("picture")
    private String icon;
    private String name;
    private String tel;
    @SerializedName("sellerid")
    private String userId;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
