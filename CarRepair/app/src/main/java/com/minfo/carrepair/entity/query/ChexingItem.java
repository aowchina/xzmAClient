package com.minfo.carrepair.entity.query;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/6/8.
 */

public class ChexingItem implements Serializable {
//    {
//        "carid": "3",
//            "cname": "奔驰A67",
//            "cimage": "http://192.168.1.120/zjxzm/images/car/1.jpg",
//            "vin": "2345678",
//            "issuedate": "1234",
//            "price": "12434",
//            "sname": "奔驰A",
//            "bname": "奔驰"
//    }
    @SerializedName("carid")
    private String id;
    @SerializedName("cname")
    private String name;
    @SerializedName("cimage")
    private String icon;
    private String price;
    private boolean choseFalg;
    @SerializedName("sname")
    private String chexi;
    @SerializedName("bname")
    private String pinpai;
    private String vin;

    public void setChexi(String chexi) {
        this.chexi = chexi;
    }

    public void setPinpai(String pinpai) {
        this.pinpai = pinpai;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getChexi() {
        return chexi;
    }

    public String getPinpai() {
        return pinpai;
    }

    public String getVin() {
        return vin;
    }

    public boolean isChoseFalg() {
        return choseFalg;
    }

    public void setChoseFalg(boolean choseFalg) {
        this.choseFalg = choseFalg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public String getPrice() {
        return price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
