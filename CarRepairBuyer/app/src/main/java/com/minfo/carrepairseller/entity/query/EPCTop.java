package com.minfo.carrepairseller.entity.query;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/6/13.
 */

public class EPCTop implements Serializable {

    @SerializedName("bname")
    private String pinpai;
    @SerializedName("sname")
    private String chexi;
    @SerializedName("cname")
    private String chexing;
    @SerializedName("cimage")
    private String icon;

    public String getPinpai() {
        return pinpai;
    }

    public String getChexi() {
        return chexi;
    }

    public String getChexing() {
        return chexing;
    }

    public String getIcon() {
        return icon;
    }

    public void setPinpai(String pinpai) {
        this.pinpai = pinpai;
    }

    public void setChexi(String chexi) {
        this.chexi = chexi;
    }

    public void setChexing(String chexing) {
        this.chexing = chexing;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
