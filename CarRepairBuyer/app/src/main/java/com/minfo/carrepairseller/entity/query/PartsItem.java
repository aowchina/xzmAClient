package com.minfo.carrepairseller.entity.query;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/6/9.
 */

public class PartsItem implements Serializable {
//    {
//        "name": "asfdg",
//            "bname": "奔驰",
//            "oem": "1234567",
//            "hprice": "0",
//            "img": "images/car/1.jpg"
//    },
    private String name;
    @SerializedName("bname")
    private String pinpai;
    private String oem;
//    @SerializedName("hprice")
    private String price;
    @SerializedName("img")
    private String icon;
    private String goodid;

    public String getGoodid() {
        return goodid;
    }

    public void setGoodid(String goodid) {
        this.goodid = goodid;
    }

    public String getName() {
        return name;
    }

    public String getPinpai() {
        return pinpai;
    }

    public String getOem() {
        return oem;
    }

    public String getPrice() {
        return price;
    }

    public String getIcon() {
        return icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPinpai(String pinpai) {
        pinpai = pinpai;
    }

    public void setOem(String oem) {
        this.oem = oem;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
